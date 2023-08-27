package tororo1066.itemdictionary

import org.bukkit.inventory.ItemStack
import tororo1066.tororopluginapi.database.SDBCondition
import tororo1066.tororopluginapi.sItem.SItem.Companion.toSItem
import java.util.Date
import java.util.UUID

class ItemData() {

    lateinit var uuid: UUID
    var category = ""
    lateinit var item: ItemStack
    var createdBy = ""
    lateinit var createdByUUID: UUID
    lateinit var createdDate: Date

    constructor(uuid: UUID, category: String, item: ItemStack, createdBy: String, createdByUUID: UUID, createdDate: Date): this(){
        this.uuid = uuid
        this.category = category
        this.item = item
        this.createdBy = createdBy
        this.createdByUUID = createdByUUID
        this.createdDate = createdDate
    }

    fun insertDB(){
        ItemDictionary.database.db.backGroundInsert("item_dictionary", mapOf(
                "uuid" to uuid.toString(),
                "category" to category,
                "item" to item.toSItem().toBase64(),
                "created_by" to createdBy,
                "created_by_uuid" to createdByUUID.toString(),
                "created_date" to createdDate
        ))
    }

    fun deleteDB(){
        ItemDictionary.database.db.backGroundDelete(
                "item_dictionary", SDBCondition().equal("uuid", uuid.toString())
        )
    }
}