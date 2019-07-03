package org.profile.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.common.service.FileUploadService;
import org.profile.service.ProfileService;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
        @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
        @Property(name = "service.vendor", value = "The Apache Software Foundation"),
        @Property(name = "sling.servlet.paths", value = { "/servlet/service/check" }),
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.extensions", value = { "serviceCheck",
                "getServiceCheck", "conference", "getService", "upload" })

})
@SuppressWarnings("serial")
public class ServiceCheck extends SlingAllMethodsServlet {

    @Reference
    private ProfileService service;

    @Reference
    private FileUploadService uploadService;

    @Override
    protected void doPost(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("serviceCheck")) {
            String status = "";
            if (request.getParameterMap().containsKey("sms")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "sms", request.getParameter("sms"));
            } else if (request.getParameterMap().containsKey("email")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "email", request.getParameter("email"));
            } else if (request.getParameterMap().containsKey("Chat")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "Chat", request.getParameter("Chat"));
            } else if (request.getParameterMap().containsKey("VChat")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "VChat", request.getParameter("VChat"));
            } else if (request.getParameterMap().containsKey("call")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "call", request.getParameter("call"));
            } else if (request.getParameterMap().containsKey("lime")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "lime", request.getParameter("lime"));
            } else if (request.getParameterMap().containsKey("ox")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "ox", request.getParameter("ox"));
            } else if (request.getParameterMap().containsKey("wc")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "wc", request.getParameter("wc"));
            } else if (request.getParameterMap().containsKey("mm")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "mm", request.getParameter("mm"));
            } else if (request.getParameterMap().containsKey("log")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "log", request.getParameter("log"));
            } else if (request.getParameterMap().containsKey("deg3")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "deg3", request.getParameter("deg3"));
            } else if (request.getParameterMap().containsKey("exam")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "exam", request.getParameter("exam"));
            } else if (request.getParameterMap().containsKey("cs")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "cs", request.getParameter("cs"));
            } else if (request.getParameterMap().containsKey("mp")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "mp", request.getParameter("mp"));
            }else if (request.getParameterMap().containsKey("camp")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "camp", request.getParameter("camp"));
            }else if (request.getParameterMap().containsKey("callMB")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "callMB", request.getParameter("callMB"));
            }else if (request.getParameterMap().containsKey("helpdesk")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "helpdesk", request.getParameter("helpdesk"));
            }else if (request.getParameterMap().containsKey("Doctiger")) {
                status = service.serviceCheck(request.getParameter("userId"),
                        "Doctiger", request.getParameter("Doctiger"));
            }
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(status);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getServiceCheck")) {
            char value = service
                    .getServiceCheck(request.getParameter("userId"),
                            request.getParameter("key"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(value);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("setCredential")) {
            String value = service.setMessageCredential(
                    request.getParameter("userId"),
                    request.getParameter("msgUser"),
                    request.getParameter("msgPassword"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(value);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("getServiceParam")) {
            String value = service.getServiceParam(
                    request.getParameter("param"),
                    request.getParameter("userId").replace("@", "_"));
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(value);
        } else if (request.getRequestPathInfo().getExtension().equals("upload")) {
            String fileName = uploadService.uploadSpSearchImage(request, 100,
                    0, "");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(fileName);
        } else if (request.getRequestPathInfo().getExtension()
                .equals("uploadV")) {
            String fileName = uploadService.uploadNThumbnail(
                    "/testing/mylibrary", request,
                    Integer.parseInt(request.getParameter("height")),
                    Integer.parseInt(request.getParameter("width")), false,
                    "staticImage");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.getOutputStream().println(fileName);
        }

    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {
        if (request.getRequestPathInfo().getExtension().equals("conference")) {

            request.getRequestDispatcher(
                    "/content/user/" + request.getParameter("userId")
                            + ".conferenceDiv").forward(request, response);

        }

    }
}
