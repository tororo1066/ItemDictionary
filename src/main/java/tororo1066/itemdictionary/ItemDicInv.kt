package tororo1066.itemdictionary

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import tororo1066.tororopluginapi.defaultMenus.CategorySInventory
import tororo1066.tororopluginapi.sInventory.SInventoryItem
import tororo1066.tororopluginapi.utils.sendMessage
import java.text.SimpleDateFormat

class ItemDicInv: CategorySInventory("ItemDictionary") {

    override fun renderMenu(p: Player): Boolean {
        if (ItemDictionary.items.isEmpty()){
            p.sendMessage(ItemDictionary.prefix + "§c登録されているアイテムがありません")
            return false
        }
        val items = LinkedHashMap<String, ArrayList<SInventoryItem>>()

        ItemDictionary.items.entries.sortedBy { it.key }.forEach { (category, data) ->
            items[category] = ArrayList(data.map {
                SInventoryItem(it.item)
                        .addLore(
                                " ",
                                "§7作成者: ${it.createdBy}",
                                "§7作成日: ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(it.createdDate)}",
                                "§cシフト右クリックで削除"
                        )
                        .setCanClick(false)
                        .setClickEvent { e ->
                            if (e.click == ClickType.SHIFT_RIGHT){
                                it.deleteDB()
                                ItemDictionary.items[category]?.remove(it)
                                if (ItemDictionary.items[category]?.isEmpty() == true){
                                    ItemDictionary.items.remove(category)
                                }
                                p.playSound(p.location, Sound.ENTITY_ITEM_BREAK, 1f, 2f)
                                allRenderMenu(p)
                            } else {
                                p.inventory.addItem(it.item)
                                p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)
                            }
                        }
            })
        }

        setCategoryName(items.keys.firstOrNull()?:"")
        setResourceItems(items)
        return true
    }
}