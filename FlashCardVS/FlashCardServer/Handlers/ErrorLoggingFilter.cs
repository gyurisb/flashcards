using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http.Filters;

namespace FlashCardServer.Handlers
{
    public class ErrorLoggingFilter : ExceptionFilterAttribute 
    {
        private static Logger errorLogger = LogManager.GetLogger("ErrorLogger");

        public override void OnException(HttpActionExecutedContext context)
        {
            var error = context.Exception;
            errorLogger.Error("request-error,[" + error.GetType() + "] \"" + error.Message + "\": " + error.StackTrace);
        }
    }
}