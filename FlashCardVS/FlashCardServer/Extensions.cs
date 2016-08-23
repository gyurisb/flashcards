using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.SqlServer;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace FlashCardServer
{
    public static class Extensions
    {
        public static IQueryable<T> RandomizeOrder<T>(this IQueryable<T> table, DbContext ctx)
        {
            if (ctx.Database.Connection is SqlConnection)
            {
                return table.OrderBy(x => Guid.NewGuid());
            }
            else if (ctx.Database.Connection is MySqlConnection)
            {
                return table.OrderBy(x => SqlFunctions.Rand());
            }
            else throw new NotImplementedException("The following SQL connection type is not supported for random order creation: " + ctx.Database.Connection.GetType());
        }
    }
}