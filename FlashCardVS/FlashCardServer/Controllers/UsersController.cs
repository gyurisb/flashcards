using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using System.Data.Entity;

namespace FlashCardServer.Models
{
    [Authorize]
    public class UsersController : ApiController
    {
        // GET: api/Users/5
        public IHttpActionResult Get(string id)
        {
            using (var ctx = new FlashCardContext())
            {
                if (id == "0")
                {
                    id = User.Identity.GetUserId();
                }

                var userEntity = ctx.AspNetUsers.Find(id);
                if (userEntity == null)
                    return NotFound();

                return Ok(new User
                {
                    ID = id,
                    DisplayName = userEntity.DisplayName,
                });
            }
        }
    }
}
