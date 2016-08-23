using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace FlashCardServer.Models
{
    //[DataContract]
    //public class FlashCard
    //{
    //    [DataMember]
    //    public int ID { get; set; }
    //    [DataMember]
    //    public string Front { get; set; }
    //    [DataMember]
    //    public string Back { get; set; }
    //    [DataMember]
    //    public int CategoryID { get; set; }
    //    [DataMember]
    //    public string Language { get; set; }
    //}

    [DataContract]
    public class FlashCards
    {
        public FlashCards(IEnumerable<int> ids)
        {
            CardIDs = ids.ToArray();
        }
        [DataMember]
        public int[] CardIDs { get; set; }
    }

    //[DataContract]
    //public class Category
    //{
    //    [DataMember]
    //    public int ID { get; set; }
    //    [DataMember]
    //    public string Name { get; set; }
    //    [DataMember]
    //    public string Language { get; set; }
    //    [DataMember]
    //    public bool IsPublic { get; set; }
    //    [DataMember]
    //    public string UserID { get; set; }
    //}

    [DataContract]
    public class User
    {
        [DataMember]
        public string ID { get; set; }
        [DataMember]
        public string DisplayName { get; set; }
    }
}