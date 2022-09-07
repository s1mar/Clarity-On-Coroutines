package com.clarity.android.interview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clarity.android.interview.viewModels.MainActivityViewModel
import com.clarity.android.interview.viewModels.MainActivityViewModel.UpdateListener

class MainActivity : AppCompatActivity() {

  private lateinit var toolbar: Toolbar
  private lateinit var recyclerView: RecyclerView
  private lateinit var adapter: ItemAdapter
  private lateinit var viewModel: MainActivityViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val itemScreenContainerView = findViewById<View>(R.id.item_screen_container)
    bindViews(itemScreenContainerView)
    viewModel = MainActivityViewModel()
    viewModel.subscribe()
    viewModel.setStateUpdateListener(object : UpdateListener {
      override fun onUpdate(state: ItemListViewState) {
        renderItemList(state)
      }
    })
  }

  override fun onDestroy() {
    viewModel.unsubscribe()
    super.onDestroy()
  }

  private fun renderItemList(state: ItemListViewState) {
    adapter.update(state.items)
    toolbar.setTitle(state.toolbarTitle)
  }

  private fun bindViews(parent: View) {
    toolbar = parent.findViewById(R.id.toolbar)

    recyclerView = parent.findViewById(R.id.recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(parent.context, RecyclerView.VERTICAL, false)

    adapter = ItemAdapter()
    recyclerView.adapter = adapter
  }
}
