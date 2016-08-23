using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;

namespace FlashCardServer
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        private static Logger errorLogger;

        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            GlobalConfiguration.Configure(WebApiConfig.Register);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
            SignConfig.Load();

            errorLogger = LogManager.GetLogger("ErrorLogger");
            this.Error += WebApiApplication_Error;
        }

        void WebApiApplication_Error(object sender, EventArgs e)
        {
            // Get the error details
            HttpException lastErrorWrapper = Server.GetLastError() as HttpException;

            Exception lastError = lastErrorWrapper;
            if (lastErrorWrapper.InnerException != null)
                lastError = lastErrorWrapper.InnerException;

            errorLogger.Error("app-error,[" + lastError.GetType() + "] \"" + lastError.Message + "\": " + lastError.StackTrace);
        }
    }
}
