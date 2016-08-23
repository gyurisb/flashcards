using FlashCardServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Runtime.Serialization;
using System.Web.Http;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using System.Data.Entity;
using System.Data.Entity.SqlServer;
using System.Data.Entity.Core.EntityClient;
using System.Data.SqlClient;

namespace FlashCardServer.Controllers
{
    [Authorize]
    public class CardsController : ApiController
    {
        // GET: api/Cards
        public IHttpActionResult Get([FromUri(Name = "category")]int? _categoryId)
        {
            using (var ctx = new FlashCardContext())
            {
                int categoryId = _categoryId.Value;

                var category = ctx.Categories.Find(categoryId);
                if (category == null)
                    return NotFound();
                if (!category.IsVisibleFor(User.Identity.GetUserId()))
                    return StatusCode(HttpStatusCode.Forbidden);

                var ids = category.FlashCards.Select(x => x.ID);
                return Ok(new FlashCards(ids));
            }
        }

        public IHttpActionResult GetRandom([FromUri(Name = "rand")]int? randCount_)
        {
            using (var ctx = new FlashCardContext())
            {

                string userId = User.Identity.GetUserId();
                int randCount = randCount_ ?? 1;
                var q = from category in ctx.Categories
                        join card in ctx.FlashCards on category.ID equals card.CategoryID
                        where category.IsPublic || category.UserID == userId || category.SharedUsers.Count(u => u.Id == userId) > 0
                        select card.ID;
                var randomCardIds = q.RandomizeOrder(ctx).Take(randCount);

                return Ok(new FlashCards(randomCardIds));
            }
        }

        // GET: api/Cards/5
        [HttpGet]
        [Route("Cards/{id}", Name = "GetCardsUrl")]
        public IHttpActionResult Get(int id)
        {
            using (var ctx = new FlashCardContext())
            {
                var card = ctx.FlashCards.Find(id);
                if (card == null)
                    return NotFound();
                if (!card.Category.IsVisibleFor(User.Identity.GetUserId()))
                    return StatusCode(HttpStatusCode.Forbidden);

                return Ok(card);
            }
        }

        // POST: api/Cards
        public IHttpActionResult Post([FromBody]FlashCard value)
        {            
            using (var ctx = new FlashCardContext())
            {
                var category = ctx.Categories.Find(value.CategoryID);
                if (category == null)
                    return NotFound();
                if (category.UserID != User.Identity.GetUserId())
                    return StatusCode(HttpStatusCode.Forbidden);

                if (value.Language == null)
                    value.Language = category.Language;

                ctx.FlashCards.Add(value);
                ctx.SaveChanges();

                return Created(
                    Url.Link("GetCardsUrl", new { id = value.ID }),
                    value
                );
            }
        }

        // PUT: api/Cards/5
        [HttpPut]
        [Route("Cards/{id}")]
        public IHttpActionResult Put(int id, [FromBody]FlashCard value)
        {
            using (var ctx = new FlashCardContext())
            {
                value.ID = id;
                string userId = User.Identity.GetUserId();

                var original = ctx.FlashCards.Find(id);
                if (original == null)
                    return NotFound();
                if (original.Category.UserID != userId)
                    return StatusCode(HttpStatusCode.Forbidden);

                var newCategory = ctx.Categories.Find(value.CategoryID);
                if (newCategory == null)
                    return NotFound();
                if (newCategory.UserID != userId)
                    return StatusCode(HttpStatusCode.Forbidden);

                if (value.Language == null)
                    value.Language = newCategory.Language;

                ctx.Entry(original).CurrentValues.SetValues(value);
                ctx.SaveChanges();

                return Ok();
            }
        }

        // DELETE: api/Cards/5
        [HttpDelete]
        [Route("Cards/{id}")]
        public IHttpActionResult Delete(int id)
        {
            using (var ctx = new FlashCardContext())
            {
                string userId = User.Identity.GetUserId();

                var removable = ctx.FlashCards.Find(id);
                if (removable == null)
                    return NotFound();
                if (removable.Category.UserID != userId)
                    return StatusCode(HttpStatusCode.Forbidden);

                ctx.FlashCards.Remove(removable);
                ctx.SaveChanges();

                return Ok();
            }
        }
    }
}
