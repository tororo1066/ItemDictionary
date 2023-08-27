package tororo1066.itemdictionary

import tororo1066.tororopluginapi.SJavaPlugin
import tororo1066.tororopluginapi.SStr

class ItemDictionary: SJavaPlugin(UseOption.SConfig) {

    companion object {
        val items = HashMap<String, ArrayList<ItemData>>()
        val prefix = SStr("§7[§6ItemDictionary§7]§r")
        lateinit var database: Database

        fun reload(){
            database = Database()
            items.clear()
            database.load()
            ItemDicCommand()
        }
    }

    override fun onStart() {
        reload()
    }
}