package com.cse.warana.controller;

import com.cse.warana.service.CompanyDocParserService;
import com.cse.warana.service.CompanyDocUploadService;
import com.cse.warana.service.CompanyTechnologyService;
import com.cse.warana.service.GetUploadedDocService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


@Controller
public class AdminPanelController {
    private static final Logger LOG = LoggerFactory.getLogger(ViewStatController.class);

    @Autowired
    @Qualifier("companyDocUploadService")
    CompanyDocUploadService docUploadService;

    @Autowired
    @Qualifier("companyDocParser")
    CompanyDocParserService companyDocParserService;

    @Value("${warana.resources.root}")
    private String root;

    @Value("${DOCUPLOADS.PATH}")
    private String uploadsPath;

    @Value("${DOCUPLOADSTEXT.PATH}")
    private String uploadsTextPath;

    @Value("${DOCTEXTOUT.PATH}")
    private String outTextPath;

    @Value("${COMPANYNAME}")
    private String companyName;

    @Autowired
    @Qualifier("companytechnologiesService")
    private CompanyTechnologyService companyTechnologyService;

    @Autowired
    @Qualifier("getCompanyDocUploadService")
    private GetUploadedDocService getUploadedDocService;


    @RequestMapping(value = "/adminpanel", method = RequestMethod.GET)
    public ModelAndView viewAdminPanel() {

        String STAT_VIEW = "adminPanel";
        ModelAndView model = new ModelAndView();
        LOG.info("View Admin Panel page Loading");
        model.setViewName(STAT_VIEW);

        return model;
    }

    @RequestMapping(value = "/adminpanel/upload", method = RequestMethod.GET)
    public ModelAndView uploadDocument() {
        String UPLOAD_VIEW = "uploadCompanyDoc";

        LOG.info("Starting company document upload view");

        ModelAndView model = new ModelAndView();
        model.setViewName(UPLOAD_VIEW);

        return model;
    }

    @RequestMapping(value = "/adminpanel/fileupload", method = RequestMethod.POST)
    public
    @ResponseBody
    String uploadFilesToServer(MultipartHttpServletRequest request) {
        LOG.info("Upload Files Started");

        ArrayList<String> fileNamesList;
        String baseUploadDirectory = root + uploadsPath;
        fileNamesList = new ArrayList<>();
        ArrayList<String> missedFiles = new ArrayList<>();
        HashMap<Object, Object> returnJson = new HashMap<>();
        boolean success = true;
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonString = "";
        new File(baseUploadDirectory).mkdir();
        Iterator<String> itr = request.getFileNames();
        MultipartFile multipartFile;
        String filePath = "";

        while (itr.hasNext()) {
            multipartFile = request.getFile(itr.next());
            filePath = multipartFile.getOriginalFilename();

            try {
                multipartFile.transferTo(new File(baseUploadDirectory + File.separator + filePath));
                fileNamesList.add(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                missedFiles.add(filePath);
                success = false;
                continue;
            }
        }

        docUploadService.storeDocData(fileNamesList);

        if (success) {
            returnJson.put("status", "true");
        } else {
            returnJson.put("status", "false");
        }


        returnJson.put("files", missedFiles);
        jsonString = gson.toJson(returnJson);
        System.out.println(jsonString);
        return jsonString;
    }

    @RequestMapping(value = "/adminpanel/processDocuments", method = RequestMethod.POST)
    @ResponseBody
    public String getTechnologyList() throws IOException {
        LOG.info("Process Document Started");
        Gson gson = new GsonBuilder().serializeNulls().create();
        String baseUploadDirectory = root + uploadsPath;
        List<String> fileNamesList = getUploadedDocService.getUploadedDocList();
        for (String filePath : fileNamesList) {
            companyDocParserService.readCompanyDoc(new File(baseUploadDirectory + File.separator + filePath), baseUploadDirectory + uploadsTextPath);
        }
        companyDocParserService.extractDoc(root, companyName);

        return gson.toJson("true");
    }
}
