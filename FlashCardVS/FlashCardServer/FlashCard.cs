//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace FlashCardServer
{
    using System;
    using System.Collections.Generic;
    using System.Runtime.Serialization;
    
    [DataContract]
    public partial class FlashCard
    {
        [DataMember]
        public int ID { get; set; }
        [DataMember]
        public int CategoryID { get; set; }
        [DataMember]
        public string Front { get; set; }
        [DataMember]
        public string Back { get; set; }
        [DataMember]
        public string Language { get; set; }
    
        public virtual Category Category { get; set; }
    }
}
