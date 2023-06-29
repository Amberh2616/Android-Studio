package com.example.myapplicationlistview1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class ListActivity : AppCompatActivity() {

    val context = this
    var listView: ListView? = null
    var myListAdapter: MyListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initializeListView()
    }

    private fun initializeListView() {
        myListAdapter = MyListAdapter()
        listView = findViewById<ListView>(R.id.listview)
        listView?.adapter = myListAdapter

        listView?.emptyView = findViewById<TextView>(R.id.empty)

        listView?.setOnItemClickListener { parent, view, position, id ->
            val r2 = listView?.adapter?.getItem(position)
            val s = "position=$position $r2"
            Log.d("@@@", s)
        }
    }

    fun click_add(view: View) {
        val r2 = Result2("NECK WIDTH", "領寬")
        myListAdapter?.stArrayList?.add(r2)
        myListAdapter?.notifyDataSetChanged()
        Toast.makeText(context, "資料新增完成", Toast.LENGTH_SHORT).show()
    }

    inner class MyListAdapter : BaseAdapter() {
        val stArrayList = arrayListOf<Result2>(
            Result2("NECK WIDTH", "領寬"),
            Result2("FRONT NECK DEPTH", "前領深"),
            Result2("NECK STRETCHED", "領寬"),
            Result2("ACROSS SHOULDER", "肩寬"),
            Result2("CHEST WIDTH", "胸寬"),
            Result2("BOTTOM OPENING", "下襬寬"),
            Result2("FRONT BODY LENGTH", "前身長"),
            Result2("ARMHOLE STRAIGHT", "袖攏直量"),
            Result2("SLEEVE LENGTH from CB", "袖長從後中量"),
            Result2("Back Neck Depth", "後領深"),
            Result2("Collar Height at Center Front", "領高前中量"),
            Result2("Collar Height at Center Back", "領高後中量")
        )

        override fun getCount(): Int {
            return stArrayList.size
        }

        override fun getItem(position: Int): Any {
            return stArrayList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val itemLayout = layoutInflater.inflate(R.layout.item_layout, null)
            val tv_item_n = itemLayout.findViewById<TextView>(R.id.tv_item_n)
            val tv_item_e = itemLayout.findViewById<TextView>(R.id.tv_item_e)
            val r2 = stArrayList[position]
            tv_item_n.text = r2.Measurement
            tv_item_e.text = r2.翻譯

            return itemLayout
        }
    }
}
