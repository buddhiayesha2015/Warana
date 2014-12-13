<%@include file="header.jsp" %>
<link href='<c:url value="/css/warana/pages-commons.css" />' rel="stylesheet" type="text/css"/>
<link href='<c:url value="/css/dropzone.css" />' rel="stylesheet" type="text/css"/>
<%--<link href='<c:url value="/css/warana/upload.css" />' rel="stylesheet" type="text/css"/>--%>

<script src='<c:url value="/js/dropzone.js" />' type="text/javascript"></script>
<script src='<c:url value="/js/warana/upload.js" />' type="text/javascript"></script>

<div id="wrapper">
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="dash-board-title">Upload Resume</h1>
                <hr style="margin-bottom:40px">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        Upload Your Resumes
                    </div>
                    <div class="panel-body">
                        <div class="table table-striped" class="files" id="previews">

                            <div id="template">
                                <!-- This is used as the file preview template -->
                                <table id="upload-info-table" class="table table-condensed" data-show-header="false">
                                    <thead>
                                        <tr>
                                            <th style="width: 20%"></th>
                                            <th style="width: 25%"></th>
                                            <th style="width: 10%"></th>
                                            <th style="width: 30%"></th>
                                            <th style="width: 10%"></th>
                                        </tr>
                                    </thead>
                                    <tr>
                                        <td>
                                            <div>
                                                <span class="preview"><img data-dz-thumbnail/></span>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <p class="name" data-dz-name></p>
                                                <strong class="error text-danger" data-dz-errormessage></strong>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <p class="size" data-dz-size></p>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="progress progress-striped active" role="progressbar"
                                                 aria-valuemin="0"
                                                 aria-valuemax="100" aria-valuenow="0">
                                                <div class="progress-bar progress-bar-success" style="width:0%;"
                                                     data-dz-uploadprogress>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <button data-dz-remove class="btn btn-danger delete">
                                                    <i class="glyphicon glyphicon-trash"></i>
                                                    <span>Remove</span>
                                                </button>
                                            </div>
                                        </td>

                                    </tr>
                                </table>
                            </div>

                        </div>
                    </div>
                    <div class="panel-footer">
                        <div id="operation-buttons-row">
                            <button type="button" id="add-files" class="btn btn-primary" style="margin-right: 10px;">
                                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                                Add Files
                            </button>
                            <button type="button" id="remove-all" class="btn btn-warning"
                                    style="margin-right: 10px;">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancel
                            </button>
                            <button type="button" class="btn btn-success" id="upload-all">
                                <span class="glyphicon glyphicon-upload" aria-hidden="true"></span> Upload
                            </button>
                        </div>
                    </div>
                </div>
                <!-- /.col-lg-4 -->
            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>