package tororo1066.itemdictionary

import tororo1066.tororopluginapi.SStr
import tororo1066.tororopluginapi.annotation.SCommandBody
import tororo1066.tororopluginapi.sCommand.*
import tororo1066.tororopluginapi.utils.sendMessage
import java.util.*

class ItemDicCommand: SCommand("itemdictionary",ItemDictionary.prefix.toString(), "itemdic.op") {

    @SCommandBody
    val open = command().setPlayerExecutor {
        ItemDicInv().open(it.sender)
    }

    @SCommandBody
    val addItem = command()
            .addArg(SCommandArg("add"))
            .addArg(SCommandArg(SCommandArgType.STRING)
                    .addChangeableAlias(object : ChangeableAlias() {
                        override fun getAlias(data: SCommandData): Collection<String> {
                            return ItemDictionary.items.keys
                        }
                    })
                    .addAlias("カテゴリ"))
            .setPlayerExecutor {
                val item = it.sender.inventory.itemInMainHand
                if (item.type.isAir) {
                    it.sender.sendMessage(ItemDictionary.prefix + "&cアイテムを手に持ってください")
                    return@setPlayerExecutor
                }

                val category = it.args[1]
                val data = ItemData(
                        UUID.randomUUID(),
                        category,
                        item,
                        it.sender.name,
                        it.sender.uniqueId,
                        Date()
                )

                data.insertDB()

                if (ItemDictionary.items.containsKey(category)){
                    ItemDictionary.items[category]!!.add(data)
                } else {
                    ItemDictionary.items[category] = arrayListOf(data)
                }

                it.sender.sendMessage(ItemDictionary.prefix + "&aアイテムを登録しました")
            }

    @SCommandBody
    val reload = command().addArg(SCommandArg("reload")).setNormalExecutor {
        ItemDictionary.reload()
        it.sender.sendMessage(ItemDictionary.prefix + "&aリロードしました")
    }
}