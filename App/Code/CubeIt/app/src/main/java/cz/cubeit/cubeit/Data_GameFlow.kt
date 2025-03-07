package cz.cubeit.cubeit

import kotlin.random.Random

object GameFlow{

    fun numberFormatString(cubeCoins: Int): String{
        return (when{
            cubeCoins > 1000000000 -> "${(cubeCoins.toDouble() / 1000000000).round(1)}B"
            cubeCoins > 1000000 -> "${(cubeCoins.toDouble() / 1000000).round(1)}M"
            cubeCoins > 1000 -> "${(cubeCoins.toDouble() / 1000).round(1)}K"
            else -> cubeCoins.toString()
        })
    }

    fun numberFormatString(cubeCoins: Long): String{
        return (when{
            cubeCoins > 1000000000 -> "${(cubeCoins.toDouble() / 1000000000).round(1)}B"
            cubeCoins > 1000000 -> "${(cubeCoins.toDouble() / 1000000).round(1)}M"
            cubeCoins > 1000 -> "${(cubeCoins.toDouble() / 1000).round(1)}K"
            else -> cubeCoins.toString()
        })
    }

    fun experienceScaleFormatString(experience: Int, player: Player?, level: Int = 1): String{
        val neededXp = player?.getRequiredXp() ?: (level * level * GenericDB.balance.playerXpRequiredLvlUpRate).toInt()
        return when {
            experience > 1000000000 -> "${(experience.toDouble() / 1000000000).round(1)}B"
            experience > 1000000 -> "${(experience.toDouble() / 1000000).round(1)}M"
            experience > 1000 -> "${(experience.toDouble() / 1000).round(1)}K"
            else -> experience.toString()
        } + " / " + when {
            neededXp > 1000000000 -> "${(neededXp.toDouble() / 1000000000).round(1)}B"
            neededXp > 1000000 -> "${(neededXp.toDouble() / 1000000).round(1)}M"
            neededXp > 1000 -> "${(neededXp.toDouble() / 1000).round(1)}K"
            else -> neededXp.toString()
        }
    }

    fun getDifficultyString(difficulty: Int): String{
        return when (difficulty) {
            0 -> "<font color='#7A7A7A'>Peaceful</font>"
            1 -> "<font color='#535353'>Easy</font>"
            2 -> "<font color='#8DD837'>Medium rare</font>"
            3 -> "<font color='#5DBDE9'>Medium</font>"
            4 -> "<font color='#058DCA'>Well done</font>"
            5 -> "<font color='#9136A2'>Hard rare</font>"
            6 -> "<font color='#FF9800'>Hard</font>"
            7 -> "<font color='#FFE500'>Evil</font>"
            else -> "Error: Collection out of its bounds! <br/> report this to the support, please."
        }
    }

    fun returnItem(playerX: Player, itemType: ItemType? = null, itemSlot: Int? = null): MutableList<Item?> {
        val allItems: MutableList<Item?> = (playerX.charClass.itemList.asSequence().plus(playerX.charClass.itemListUniversal.asSequence())).toMutableList()
        //val allowedItems: MutableList<Item?> = mutableListOf()

        //allItems.sortWith(compareBy { it!!.levelRq })
        allItems.retainAll { playerX.level > it!!.levelRq - GenericDB.balance.itemLvlGenBottom && playerX.level < it.levelRq + GenericDB.balance.itemLvlGenTop}
        if(itemType != null){
            allItems.retainAll { it!!.type == itemType}
        }
        if(itemSlot != null){
            allItems.retainAll { it!!.slot == itemSlot}
        }

        return allItems
    }

    fun generateItem(playerG: Player, inQuality: Int? = null, itemType: ItemType? = null, itemSlot: Int? = null, itemLevel: Int? = null, isNPC: Boolean = false): Item? {

        val tempArray: MutableList<Item?> = returnItem(playerG, itemType, itemSlot)
        if(tempArray.size == 0){
            return null
        }

        val itemReturned = tempArray[Random.nextInt(0, tempArray.size)]
        val itemTemp: Item? = when (itemReturned?.type) {
            ItemType.Weapon -> Weapon(
                    name = itemReturned.name,
                    type = itemReturned.type,
                    charClass = itemReturned.charClass,
                    description = itemReturned.description,
                    levelRq = itemReturned.levelRq,
                    bitmapId = itemReturned.bitmapId,
                    slot = itemReturned.slot
            )
            ItemType.Wearable -> Wearable(
                    name = itemReturned.name,
                    type = itemReturned.type,
                    charClass = itemReturned.charClass,
                    description = itemReturned.description,
                    levelRq = itemReturned.levelRq,
                    bitmapId = itemReturned.bitmapId,
                    slot = itemReturned.slot
            )
            ItemType.Runes -> Runes(
                    name = itemReturned.name,
                    type = itemReturned.type,
                    charClass = itemReturned.charClass,
                    description = itemReturned.description,
                    levelRq = itemReturned.levelRq,
                    bitmapId = itemReturned.bitmapId,
                    slot = itemReturned.slot
            )
            else -> Item(name = itemReturned!!.name, type = itemReturned.type, charClass = itemReturned.charClass, description = itemReturned.description, levelRq = itemReturned.levelRq, bitmapId = itemReturned.bitmapId, slot = itemReturned.slot)
        }
        itemTemp!!.levelRq = itemLevel ?: Random.nextInt(playerG.level - 2, playerG.level + 1)
        if (inQuality == null) {
            itemTemp.quality = when (Random.nextInt(0, (GenericDB.balance.itemQualityPerc["7"] ?: error("")) + 1)) {                   //quality of an item by percentage
                in 0 until (GenericDB.balance.itemQualityPerc["0"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["0"] ?: error("")        //39,03%
                in (GenericDB.balance.itemQualityPerc["0"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["1"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["1"] ?: error("")     //27%
                in (GenericDB.balance.itemQualityPerc["1"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["2"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["2"] ?: error("")     //20%
                in (GenericDB.balance.itemQualityPerc["2"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["3"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["3"] ?: error("")     //8,41%
                in (GenericDB.balance.itemQualityPerc["3"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["4"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["4"] ?: error("")     //5%
                in (GenericDB.balance.itemQualityPerc["4"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["5"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["5"] ?: error("")     //0,5%
                in (GenericDB.balance.itemQualityPerc["5"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["6"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["6"] ?: error("")     //0,08%
                in (GenericDB.balance.itemQualityPerc["6"] ?: error("")) + 1 until (GenericDB.balance.itemQualityPerc["7"] ?: error("")) -> GenericDB.balance.itemQualityGenImpact["7"] ?: error("")    //0,01%
                else -> 0
            }
        } else {
            itemTemp.quality = kotlin.math.min(7, inQuality)
        }

        if (itemTemp.levelRq < 1) itemTemp.levelRq = 1
        var points = Random.nextInt(itemTemp.levelRq * 3 * (itemTemp.quality + 1), itemTemp.levelRq * 3 * ((itemTemp.quality * 1.25).toInt() + 2))
        var pointsTemp: Int
        itemTemp.priceCubeCoins = points.toLong()
        val numberOfStats = Random.nextInt(1, 9)
        for (i in 0..numberOfStats) {
            pointsTemp = if(i == numberOfStats) points else Random.nextInt(points / (numberOfStats * 2), points / numberOfStats + 1)
            when (itemTemp) {
                is Weapon -> {
                    val index = if(isNPC){
                        pointsTemp = points / 4
                        i
                    }else  Random.nextInt(0, if (playerG.charClass.lifeSteal) 4 else 3)

                    when (index) {
                        0 -> {
                            itemTemp.power += (pointsTemp * (GenericDB.balance.itemGenRatio["Power"] ?: 1.0)).toInt()
                        }
                        1 -> {
                            itemTemp.block += (pointsTemp * (GenericDB.balance.itemGenRatio["Block"] ?: 1.0)).toInt()
                        }
                        2 -> {
                            itemTemp.dmgOverTime += (pointsTemp * (GenericDB.balance.itemGenRatio["DamageOverTime"] ?: 1.0)).toInt()
                        }
                        3 -> {
                            itemTemp.lifeSteal += (pointsTemp * (GenericDB.balance.itemGenRatio["LifeSteal"] ?: 1.0)).toInt()
                        }
                    }
                }
                is Wearable -> {
                    val index = if(isNPC){
                        pointsTemp = points / 4
                        i
                    }else  Random.nextInt(0, 4)

                    when (index) {
                        0 -> {
                            itemTemp.armor += (pointsTemp * (GenericDB.balance.itemGenRatio["Armor"] ?: 1.0)).toInt()
                        }
                        1 -> {
                            itemTemp.block += (pointsTemp * (GenericDB.balance.itemGenRatio["Block"] ?: 1.0)).toInt()
                        }
                        2 -> {
                            itemTemp.health += (pointsTemp * (GenericDB.balance.itemGenRatio["Health"] ?: 1.0)).toInt()
                        }
                        3 -> {
                            itemTemp.energy += (pointsTemp * (GenericDB.balance.itemGenRatio["Energy"] ?: 1.0)).toInt()
                        }
                    }
                }
                is Runes -> {
                    val index = if(isNPC){
                        pointsTemp = points / 4
                        i
                    }else  Random.nextInt(0, 4)

                    when (index) {
                        0 -> {
                            itemTemp.armor += (pointsTemp * (GenericDB.balance.itemGenRatio["Armor"] ?: 1.0)).toInt()
                        }
                        1 -> {
                            itemTemp.health += (pointsTemp * (GenericDB.balance.itemGenRatio["Health"] ?: 1.0)).toInt()
                        }
                        2 -> {
                            itemTemp.adventureSpeed += (pointsTemp * (GenericDB.balance.itemGenRatio["AdventureSpeed"] ?: 1.0)).toInt()
                        }
                        3 -> {
                            if((itemTemp.inventorySlots + (pointsTemp * (GenericDB.balance.itemGenRatio["InventorySlots"] ?: 1.0)).toInt()) > playerG.level / 2){        //limit the inventory slots by player's level
                                points += pointsTemp - kotlin.math.abs(((itemTemp.inventorySlots - (playerG.level / 2)) / (GenericDB.balance.itemGenRatio["InventorySlots"] ?: 1.0)).toInt())
                                itemTemp.inventorySlots += kotlin.math.abs(itemTemp.inventorySlots - (playerG.level / 2))
                            }else {
                                itemTemp.inventorySlots += (pointsTemp * (GenericDB.balance.itemGenRatio["InventorySlots"] ?: 1.0)).toInt()
                            }
                        }
                    }
                }
            }
            points -= pointsTemp
        }
        return itemTemp
    }
}