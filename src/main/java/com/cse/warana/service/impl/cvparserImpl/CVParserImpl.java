package com.cse.warana.service.impl.cvparserImpl;

import com.cse.warana.service.cvparser.CVParser;

import com.cse.warana.service.impl.cvparserImpl.infoExtractorsImpl.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import impl.infoExtractors.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Nadeeshaan on 11/7/2014.
 */

public class CVParserImpl implements CVParser {

    /**
     * Initialize the global variables
     */
    public static ArrayList<String> EducationalHeadings = new ArrayList<String>();
    public static ArrayList<String> ProfileHeadings = new ArrayList<String>();
    public static ArrayList<String> WorkHistoryHeadings = new ArrayList<String>();
    public static ArrayList<String> AwardsAndAchievementsHeadings = new ArrayList<String>();
    public static ArrayList<String> ProjectsHeadings = new ArrayList<String>();
    public static ArrayList<String> lines = new ArrayList<String>();
    public static ArrayList<String> linesCopy = new ArrayList<String>();
    public static ArrayList<String> indexedLines = new ArrayList<String>();

    public static String[] docLines = null;
    public static HashMap<String, ArrayList<Integer>> sectionMap = new HashMap<String, ArrayList<Integer>>();

    public static AbstractSequenceClassifier<CoreLabel> classifier = null;


    /**
     * Constructor
     */
    public CVParserImpl() {
        /**
         * Load the 7 class classifier
         * Finds Time, Location, Organization, Person, Money, Percent, Date
         */
        String serializedClassifier = "classifiers/english.muc.7class.distsim.crf.ser.gz";
        try {
            classifier = CRFClassifier.getClassifier(serializedClassifier);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * In this method, tokens are loaded to the memory(ArrayList)
     * These tokens are used to identify the possible heading lines
     */
    @Override
    public void initializeHeadingTokens() {

        /**
         * TODO INITIALIZE THE ABOVE HEADING LISTS
         */
        String token = "";
        try {
            BufferedReader EduBr = new BufferedReader(new FileReader("input/eduTokens"));
            BufferedReader ProfsBr = new BufferedReader(new FileReader("input/profTokens"));
            BufferedReader WrkBr = new BufferedReader(new FileReader("input/wrkTokens"));
            BufferedReader AwrdBr = new BufferedReader(new FileReader("input/awardsTokens"));
            BufferedReader ProjBr = new BufferedReader(new FileReader("input/projTokens"));

            token = EduBr.readLine();

            while (!token.equals("end")) {
                EducationalHeadings.add(token);
                token = EduBr.readLine();
            }

            token = ProfsBr.readLine();

            while (!token.equals("end")) {
                ProfileHeadings.add(token);
                token = ProfsBr.readLine();
            }

            token = WrkBr.readLine();

            while (!token.equals("end")) {
                WorkHistoryHeadings.add(token);
                token = WrkBr.readLine();
            }

            token = AwrdBr.readLine();

            while (!token.equals("end")) {
                AwardsAndAchievementsHeadings.add(token);
                token = AwrdBr.readLine();
            }

            token = ProjBr.readLine();

            while (!token.equals("end")) {
                ProjectsHeadings.add(token);
                token = ProjBr.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to identify the sections based on the pre defined tokens
     * specific to the sections
     */
    @Override
    public void identifyHeadings() {
        String line = "";
        boolean allFalse = false;
        Pattern pattern = null;
        Matcher matcher = null;

        for (int a = 0; a < lines.size(); a++) {
            line = lines.get(a);

            // Assume that a heading cannot be larger than three words
            /**
             * TODO need to remove the stop words before the processing
             */
            if (line.split(" ").length <= 5) {
                for (int ctr = 0; ctr < EducationalHeadings.size(); ctr++) {

                    // Section is named under EDU_INF
                    pattern = Pattern.compile(".*" + EducationalHeadings.get(ctr) + ".*");
                    matcher = pattern.matcher(line.toLowerCase());
                    if (matcher.matches()) {
                        if (sectionMap.containsKey("EDU_INFO")) {
                            if (!(sectionMap.get("EDU_INFO")).contains(new Integer(a))) {
                                (sectionMap.get("EDU_INFO")).add(new Integer(a));
                            }
                        } else {
                            ArrayList<Integer> l = new ArrayList<Integer>();
                            l.add(new Integer(a));
                            sectionMap.put("EDU_INFO", l);
                        }
                        indexedLines.add(String.valueOf(a));

                        System.out.println(line + "-----Matched to EDUCATIONAL INFO");
                    }
                }

                for (int ctr = 0; ctr < ProfileHeadings.size(); ctr++) {

                    // Section is named under PROF_INF
                    pattern = Pattern.compile(".*" + ProfileHeadings.get(ctr) + ".*");
                    matcher = pattern.matcher(line.toLowerCase());

                    if (matcher.matches()) {
                        if (sectionMap.containsKey("PROF_INFO")) {
                            if (!(sectionMap.get("PROF_INFO")).contains(new Integer(a))) {
                                (sectionMap.get("PROF_INFO")).add(new Integer(a));
                            }
                        } else {
                            ArrayList<Integer> l = new ArrayList<Integer>();
                            l.add(new Integer(a));
                            sectionMap.put("PROF_INFO", l);
                        }
                        indexedLines.add(String.valueOf(a));

                        System.out.println(line + "-----Matched to PROFILE INFO");
                    }
                }

                for (int ctr = 0; ctr < WorkHistoryHeadings.size(); ctr++) {
                    pattern = Pattern.compile(".*" + WorkHistoryHeadings.get(ctr) + ".*");
                    matcher = pattern.matcher(line.toLowerCase());

                    if (matcher.matches()/* && !sectionMap.containsKey("EDU_INFO")*/) {
                        if (sectionMap.containsKey("WRK_INFO")) {
                            if (!(sectionMap.get("WRK_INFO")).contains(new Integer(a))) {
                                (sectionMap.get("WRK_INFO")).add(new Integer(a));
                            }
                        } else {
                            ArrayList<Integer> l = new ArrayList<Integer>();
                            l.add(new Integer(a));
                            sectionMap.put("WRK_INFO", l);
                        }
                        indexedLines.add(String.valueOf(a));

                        System.out.println(line + "-----Matched to Work INFO");
                    }
                }

                for (int ctr = 0; ctr < AwardsAndAchievementsHeadings.size(); ctr++) {
                    pattern = Pattern.compile(".*" + AwardsAndAchievementsHeadings.get(ctr) + ".*");
                    matcher = pattern.matcher(line.toLowerCase());

                    if (matcher.matches()/* && !sectionMap.containsKey("EDU_INFO")*/) {
                        if (sectionMap.containsKey("AWRD_INFO")) {
                            if (!(sectionMap.get("AWRD_INFO")).contains(new Integer(a))) {
                                (sectionMap.get("AWRD_INFO")).add(new Integer(a));
                            }
                        } else {
                            ArrayList<Integer> l = new ArrayList<Integer>();
                            l.add(new Integer(a));
                            sectionMap.put("AWRD_INFO", l);
                        }
                        indexedLines.add(String.valueOf(a));

                        System.out.println(line + "-----Matched to AWARDS INFO");
                    }
                }

                for (int ctr = 0; ctr < ProjectsHeadings.size(); ctr++) {
                    pattern = Pattern.compile(".*" + ProjectsHeadings.get(ctr) + ".*");
                    matcher = pattern.matcher(line.toLowerCase());

                    if (matcher.matches()/* && !sectionMap.containsKey("EDU_INFO")*/) {
                        if (sectionMap.containsKey("PROJ_INFO")) {
                            if (!(sectionMap.get("PROJ_INFO")).contains(new Integer(a))) {
                                (sectionMap.get("PROJ_INFO")).add(new Integer(a));
                            }
                        } else {
                            ArrayList<Integer> l = new ArrayList<Integer>();
                            l.add(new Integer(a));
                            sectionMap.put("PROJ_INFO", l);
                        }
                        indexedLines.add(String.valueOf(a));

                        System.out.println(line + "-----Matched to PROJECTS INFO");
                    }
                }
            }

            pattern = Pattern.compile(".*referee.*");
            matcher = pattern.matcher(line.toLowerCase());

            if (matcher.matches()) {
                if (sectionMap.containsKey("REF_INFO")) {
                    if (!(sectionMap.get("REF_INFO")).contains(new Integer(a))) {
                        (sectionMap.get("REF_INFO")).add(new Integer(a));
                    }
                } else {
                    ArrayList<Integer> l = new ArrayList<Integer>();
                    l.add(new Integer(a));
                    sectionMap.put("REF_INFO", l);
                }
                indexedLines.add(String.valueOf(a));

                System.out.println(line + "-----Matched to REFEREE INFO");
            }
        }
    }


    /**
     * This method is used to classify the lines under the headings and extract the
     * required information
     */
    @Override
    public void parseLines() {
        EducationalInfoExtractImpl eduInfo = new EducationalInfoExtractImpl();
        PersonalInfoExtractImpl perInfo = new PersonalInfoExtractImpl();
        WorkInfoExtractImpl wrkInfo = new WorkInfoExtractImpl(classifier);
        AchievementsInfoExtractImpl achInfo = new AchievementsInfoExtractImpl();
        ProjectInfoExtractionImpl projInfo = new ProjectInfoExtractionImpl();
        RefereeInfoExtractImpl refInfo = new RefereeInfoExtractImpl(classifier);
        FindMissedInfoImpl missedInfo = new FindMissedInfoImpl();

        String previous = "";
        Iterator it = sectionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            if (pairs.getKey().equals("EDU_INFO")) {
                eduInfo.extractEduInformation(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            } else if (pairs.getKey().equals("PROF_INFO")) {
                perInfo.extractPersonalInformation(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            } else if (pairs.getKey().equals("WRK_INFO")) {
                wrkInfo.extractWorkInfo(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            } else if (pairs.getKey().equals("AWRD_INFO")) {
                achInfo.extractAchievementInformation(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            } else if (pairs.getKey().equals("PROJ_INFO")) {
                projInfo.extractProjectInfo(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            } else if (pairs.getKey().equals("REF_INFO")) {
                refInfo.getRefereeInfo(lines, (ArrayList<Integer>) pairs.getValue(), indexedLines, linesCopy);
            }
        }
        missedInfo.findProfileInfo(linesCopy);
    }


    /**
     * Read the pdf
     *
     * @param file
     */
    @Override
    public void readPdfDocument(File file) {
        PDFParser parser = null;
        String text = "";
        PDFTextStripper stripper = null;
        PDDocument pdoc = null;
        COSDocument cdoc = null;
        try {
            parser = new PDFParser(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parser.parse();
            cdoc = parser.getDocument();
            stripper = new PDFTextStripper();
            pdoc = new PDDocument(cdoc);
            stripper.setStartPage(1);
            stripper.setEndPage(4);
            text = stripper.getText(pdoc);

            docLines = text.split("(\r\n)");

            for (int a = 0; a < docLines.length; a++) {
                String s = docLines[a];
                if (!s.equals(" ")) {
                    s = s.replaceAll("[^\\w\\s\\@\\_\\.\\,\\(\\)\\:\\-\\!\\#\\$\\%\\\\&\\*\\+\\=]", "");
                    lines.add(s.trim());
                    // Keep a copy of the lines in order to remove them and keep track of the missed lines
                    // to extract information form them during the missed info extraction phase
                    linesCopy.add(s.trim());
                }
            }

            for (int a = 0; a < lines.size(); a++) {
                System.out.println(lines.get(a));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cdoc.close();
        }
    }
}
