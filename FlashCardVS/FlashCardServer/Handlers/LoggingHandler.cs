using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;

namespace FlashCardServer.Handlers
{
    public class LoggingHandler : DelegatingHandler
    {
        private static Logger requestLogger = LogManager.GetLogger("RequestLogger");
        private static Logger applicationLogger = LogManager.GetLogger("ApplicationLogger");

        public LoggingHandler()
        {
            applicationLogger.Info("LoggingHandler registered");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
                applicationLogger.Info("LoggingHandler disposing");
            else
                applicationLogger.Info("LoggingHandler not disposing");
        }

        protected async override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            var response = await base.SendAsync(request, cancellationToken);
            requestLogger.Info(CreateMessage(request, response));
            return response;
        }
    
        public static string CreateMessage(HttpRequestMessage requestMessage, HttpResponseMessage responseMessage, params object[] args)
        {
            StringBuilder builder = new StringBuilder();
            builder.Append(requestMessage.Method)
                .Append(',').Append(requestMessage.RequestUri)
                .Append(',').Append(requestMessage.GetOwinContext().Request.RemoteIpAddress)
                .Append(',').Append(requestMessage.GetOwinContext().Request.User.Identity.GetUserId())
                .Append(',').Append(responseMessage.StatusCode);
            foreach (object arg in args)
            {
                if (arg is string)
                {
                    builder.Append(",\"").Append(arg).Append('"');
                }
                else
                {
                    builder.Append(",").Append(arg);
                }
            }
            return builder.ToString();
        }
    }
}