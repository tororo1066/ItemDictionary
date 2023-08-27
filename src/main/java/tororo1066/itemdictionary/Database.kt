package tororo1066.itemdictionary

import tororo1066.tororopluginapi.SJavaPlugin
import tororo1066.tororopluginapi.database.SDBVariable
import tororo1066.tororopluginapi.database.SDatabase
import tororo1066.tororopluginapi.sItem.SItem
import tororo1066.tororopluginapi.utils.toDate
import java.util.*

class Database {

    val db = SDatabase.newInstance(SJavaPlugin.plugin)

    init {
        db.createTable("item_dictionary", mapOf(
                "id" to SDBVariable(SDBVariable.Int, false, index = SDBVariable.Index.PRIMARY),
                "uuid" to SDBVariable(SDBVariable.VarChar, 36, index = SDBVariable.Index.UNIQUE),
                "category" to SDBVariable(SDBVariable.Text),
                "item" to SDBVariable(SDBVariable.LongText),
                "created_by" to SDBVariable(SDBVariable.VarChar, 16),
                "created_by_uuid" to SDBVariable(SDBVariable.VarChar, 36),
                "created_date" to SDBVariable(SDBVariable.DateTime)
        ))
    }

    fun load(){
        val result = db.asyncSelect("item_dictionary").get()
        result.forEach { rs ->
            val item = ItemData(
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("category"),
                    SItem.fromBase64(rs.getString("item"))!!.asItemStack(),
                    rs.getString("created_by"),
                    UUID.fromString(rs.getString("created_by_uuid")),
                    if (db.isMongo){
                        rs.getDate("created_date")
                    } else {
                        rs.getLocalDateTime("created_date").toDate()
                    }
            )

            if (ItemDictionary.items.containsKey(item.category)){
                ItemDictionary.items[item.category]!!.add(item)
            } else {
                ItemDictionary.items[item.category] = arrayListOf(item)
            }
        }
    }
}