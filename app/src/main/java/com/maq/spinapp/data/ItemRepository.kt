package com.maq.spinapp.data

import com.maq.spinapp.database.Itemdao


class ItemRepository(
    private val itemdao: Itemdao
) {

    fun insertItems(item: Item) =  itemdao.insert(item)

    fun getItemsDb() =  itemdao.getAll()

    fun getCount() =  itemdao.getRowCount()


}