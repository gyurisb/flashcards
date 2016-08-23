using FlashCardServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FlashCardServer
{
    public partial class Category
    {
        public bool IsVisibleFor(string userId)
        {
            return IsPublic || UserID == userId || SharedUsers.Count(u => u.Id == userId) > 0;
        }
    }

    public static class FlashCardRepository
    {
        //public static List<Category> Categories = new Category[]
        //{
        //    new Category
        //    {
        //        ID = 1,
        //        Name = "Állatok",
        //        Language = "en",
        //        IsPublic = true,
        //        UserID = "user1"
        //    }
        //}.ToList();

        //public static List<FlashCard> FlashCards = new FlashCard[]
        //{
        //    new FlashCard
        //    {
        //        ID = 1,
        //        Front = "kutya",
        //        Back = "dog",
        //        CategoryID = 1,
        //        Language = "en"
        //    },
        //    new FlashCard
        //    {
        //        ID = 2,
        //        Front = "macska",
        //        Back = "cat",
        //        CategoryID = 1,
        //        Language = "en"
        //    },
        //    new FlashCard
        //    {
        //        ID = 3,
        //        Front = "tehén",
        //        Back = "cow",
        //        CategoryID = 1,
        //        Language = "en"
        //    },
        //    new FlashCard
        //    {
        //        ID = 4,
        //        Front = "kacsa",
        //        Back = "duck",
        //        CategoryID = 1,
        //        Language = "en"
        //    }
        //}.ToList();

        //public static List<User> Users = new User[]
        //{
        //    new User
        //    {
        //        ID = "user1",
        //        DisplayName = "Gyuris Bence"
        //    }
        //}.ToList();
    }
}