using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Web;

namespace FlashCardServer
{
    public static class SignConfig
    {
        private static RSACryptoServiceProvider rsaApplicationClient;

        public static void Load()
        {
            string serverkeyFile = HttpContext.Current.Server.MapPath("~/serverkey.config");
            string serverKeyXml;
            using (var reader = new StreamReader(serverkeyFile))
            {
                serverKeyXml = reader.ReadToEnd();
            }
            rsaApplicationClient = new RSACryptoServiceProvider();
            rsaApplicationClient.FromXmlString(serverKeyXml);
        }

        public static byte[] SignData(byte[] data)
        {
            return rsaApplicationClient.SignData(data, new System.Security.Cryptography.SHA1Managed());
        }

        public static bool Verify(byte[] data, byte[] signature)
        {
            try
            {
                return rsaApplicationClient.VerifyData(data, new System.Security.Cryptography.SHA1Managed(), signature);
            }
            catch (Exception) { }
            return false;
        }

    }
}