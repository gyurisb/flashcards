using FlashCardServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using System.Data.Entity;
using System.Security.Cryptography;

namespace FlashCardServer.Controllers
{
    [Authorize]
    public class CategoriesController : ApiController
    {
        // GET: api/Categories
        [Route("Categories")]
        public IEnumerable<Category> Get([FromUri]bool own = false)
        {
            using (var ctx = new FlashCardContext())
            {
                //var user = UserManager.FindById(User.Identity.GetUserId());
                string userId = User.Identity.GetUserId();
                if (own)
                    return ctx.Categories.Where(cat => cat.UserID == userId).ToList();
                else
                    return ctx.Categories.Where(cat => cat.IsPublic || cat.UserID == userId || cat.SharedUsers.Count(u => u.Id == userId) > 0).ToList();
                //return ctx.Categories.Where(cat => cat.IsVisibleFor(userId)).ToList();
            }
        }

        // GET: api/Categories/5
        [HttpGet]
        [Route("Categories/{id}", Name = "GetCategoryUrl")]
        public IHttpActionResult Get(int id)
        {
            using (var ctx = new FlashCardContext())
            {
                string userId = User.Identity.GetUserId();

                var category =  ctx.Categories.Find(id);
                if (category == null)
                    return NotFound();

                //if (!category.IsPublic && category.UserID != userId && category.SharedUsers.Count(u => u.Id == userId) == 0)
                if (!category.IsVisibleFor(userId))
                    return StatusCode(HttpStatusCode.Forbidden);

                return Ok(category);
            }
        }

        // GET: api/Categories/5?share
        [HttpGet]
        [Route("Categories/{id}")]
        public IHttpActionResult GetShare(int id, [FromUri]bool? share)
        {
            using (var ctx = new FlashCardContext())
            {
                var category = ctx.Categories.Find(id);
                if (category == null)
                    return NotFound();
                if (category.UserID != User.Identity.GetUserId())
                    return StatusCode(HttpStatusCode.Forbidden);
                if (category.IsPublic)
                    return StatusCode(HttpStatusCode.BadRequest);

                byte[] operationPrefix = new byte[] { 10 };
                byte[] shareData = BitConverter.GetBytes(id);
                byte[] data = operationPrefix.Concat(shareData).ToArray();

                byte[] signedData = SignConfig.SignData(data);
                string key = Convert.ToBase64String(signedData);

                return Ok(key);
            }
        }

        [HttpGet]
        [AllowAnonymous]
        [Route("Categories/{id}")]
        public string GetKeyRedirect(int id, [FromUri]string key)
        {
            return "Ez az oldal a Szókártya alkalmazással érhető el.";
        }

        // POST: api/Categories
        [Route("Categories")]
        public IHttpActionResult Post([FromBody]Category value)
        {
            using (var ctx = new FlashCardContext())
            {
                string userId = User.Identity.GetUserId();
                value.UserID = userId;

                ctx.Categories.Add(value);
                ctx.SaveChanges();

                return Created(
                    Url.Link("GetCategoryUrl", new { id = value.ID }),
                    value
                    );
            }
        }

        // PUT: api/Categories/5
        [HttpPut]
        [Route("Categories/{id}")]
        public IHttpActionResult Put(int id, [FromBody]Category value)
        {
            using (var ctx = new FlashCardContext())
            {
                string userId = User.Identity.GetUserId();
                value.UserID = userId;
                value.ID = id;

                var original = ctx.Categories.Find(id);
                if (original == null)
                    return NotFound();
                if (original.UserID != userId)
                    return StatusCode(HttpStatusCode.Forbidden);

                ctx.Entry(original).CurrentValues.SetValues(value);
                ctx.SaveChanges();

                return Ok();
            }
        }

        // PUT: api/Categories/5?unlock
        [HttpPut]
        [Route("Categories/{id}")]
        public IHttpActionResult PutUnlock(int id, [FromUri]bool? unlock, [FromBody]string key)
        {
            using (var ctx = new FlashCardContext())
            {
                if (key == null)
                    return StatusCode(HttpStatusCode.Forbidden);
                string userId = User.Identity.GetUserId();

                var category = ctx.Categories.Find(id);

                if (category == null)
                    return NotFound();
                if (category.IsVisibleFor(userId))
                    return StatusCode(HttpStatusCode.BadRequest);

                byte[] keyData;
                try
                {
                    keyData = Convert.FromBase64String(key);
                }
                catch (Exception)
                {
                    return StatusCode(HttpStatusCode.Forbidden);
                }

                byte[] operationPrefix = new byte[] { 10 };
                byte[] shareData = BitConverter.GetBytes(id);
                byte[] data = operationPrefix.Concat(shareData).ToArray();

                if (SignConfig.Verify(data, keyData))
                {
                    var currentUser = ctx.AspNetUsers.Find(userId);
                    category.SharedUsers.Add(currentUser);
                    ctx.SaveChanges();
                    return Ok();
                }
                else return StatusCode(HttpStatusCode.Forbidden);
            }
        }

        // DELETE: api/Categories/5
        [HttpDelete]
        [Route("Categories/{id}")]
        public IHttpActionResult Delete(int id)
        {
            using (var ctx = new FlashCardContext())
            {
                string userId = User.Identity.GetUserId();

                var removable = ctx.Categories.Find(id);
                if (removable == null)
                    return NotFound();
                if (removable.UserID != userId)
                    return StatusCode(HttpStatusCode.Forbidden);

                ctx.Categories.Remove(removable);
                ctx.SaveChanges();

                return Ok();
            }
        }
    }
}
