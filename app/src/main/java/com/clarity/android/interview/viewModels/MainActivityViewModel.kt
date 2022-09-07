package com.clarity.android.interview.viewModels

import android.util.Log
import com.clarity.android.interview.ItemListViewState
import com.clarity.android.interview.ItemRow
import com.clarity.android.interview.network.DeliveryItem
import com.clarity.android.interview.network.NetworkService
import com.clarity.android.interview.network.OrderResponse
import io.reactivex.annotations.Nullable
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel : CoroutineScope {

  private var parentJob = Job()
  private val TAG : String = MainActivityViewModel::class.java.simpleName

  private val networkService by lazy {
    NetworkService()
  }

  interface UpdateListener {
    fun onUpdate(state: ItemListViewState)
  }

  private var itemListViewState: ItemListViewState
  private var listener: UpdateListener? = null

  init {
    val items = listOf(
        ItemRow("Cabbage"),
        ItemRow("Apple"),
        ItemRow("Bread")
    )

    itemListViewState = ItemListViewState("Delivery Items", items)

  }

  private suspend fun makeNetworkCall(){
      try {
        val ordersResponse = networkService.api.fetchOrdersSuspendable()
        val listOfAllDeliveryItems = mutableListOf<DeliveryItem>()
        ordersResponse.orders.forEach{orderId->
          listOfAllDeliveryItems.addAll(networkService.api
            .fetchOrderByIdSuspendable(orderId).items)
        }
        val distinctNames = getUniqueOrderNames(listOfAllDeliveryItems)
        updateItemListViewState(distinctNames)

      }catch (ex:Exception){
        Log.d(TAG,"Exception occurred during network requests")
      }
  }

  private suspend fun updateItemListViewState(distinctItems:List<ItemRow>){
       itemListViewState = ItemListViewState(toolbarTitle = "Updated List",distinctItems)
       withContext(Dispatchers.Main){
            listener?.onUpdate(itemListViewState)
       }
  }

  private fun getUniqueOrderNames(deliveryItemList : List<DeliveryItem>): List<ItemRow>{
      val listOfNames =  deliveryItemList.map { deliveryItem ->
        deliveryItem.name
      }
    return listOfNames.distinct().map { ItemRow(it) }
  }

  fun setStateUpdateListener(@Nullable listener: UpdateListener?) {
    this.listener = listener

    listener?.onUpdate(itemListViewState)
  }


  fun subscribe(){
    parentJob = Job()
    launch {
      makeNetworkCall()
    }
  }

  fun unsubscribe(){
    parentJob.cancel(CancellationException("Life cycle considerations,cancelling job"))
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + parentJob


}
