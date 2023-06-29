package com.example.myapplicationlistview

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

        初始化ListView()
    }

    private fun 初始化ListView() {
// 資料 適配器
        myListAdapter = MyListAdapter()
// 尋找 ListView
        listView = findViewById<ListView>(R.id.listview)
// 設定 ListView 資料適配器
        listView?.adapter = myListAdapter

// 設定 資料0筆時要顯示哪個View (通常用TextView顯示, 目前沒有資料)
        listView?.emptyView = findViewById<TextView>(R.id.empty)

// 設定(監聽器) 點選項目 OnItemClick, 要執行的程式
        listView?.setOnItemClickListener { parent, view, position, id ->
            val r2 = listView?.adapter?.getItem(position)
            val s = "position=" + position + " " + r2.toString()
            Log.d("@@@", s)
        }
    }

    fun click_add(view: View) {
// 圖片陣列 新增 p4 圖片id
        myListAdapter?.drawableArrayList?.add(R.drawable.p4)
// 學生陣列 新增 Mary
        val r2 = Result2 ("Mary", 86, 83)
        myListAdapter?.stArrayList?.add(r2)
// 資料有異動,通知 ListView 畫面需更新
        myListAdapter?.notifyDataSetChanged()
// 顯示 短訊息
        Toast.makeText(context, "資料新增完成", Toast.LENGTH_SHORT).show()
    }

    // inner 內部類別 可使用外部類別資料,包含 private 資料
    inner class MyListAdapter: BaseAdapter() { // 繼承 BaseAdapter
        // 圖片陣列: arrayListOf() 快速建立ArrayList資料
        val drawableArrayList = arrayListOf<Int>( R.drawable.p1,
            R.drawable.p2, R.drawable.p3,
        )

        // 學生陣列: arrayListOf() 快速建立ArrayList資料
        val stArrayList = arrayListOf<Result2>(
            Result2("JWS",100, 100),
            Result2("Amber", 90, 95),
            Result2("Jack", 75, 80),
        )

        // 資料數量 (由 ListView 呼叫. 也稱為 Callback 函式)
        override fun getCount(): Int {
            return stArrayList.size // 陣列大小 即 資料數量
        }

        // 取得  某項目資料(學生) (由 ListView 呼叫,也稱為 Callback 函式)
        override fun getItem(position: Int): Any {
            val r2 = stArrayList.get(position)
            return r2
        }

        // 取得  某項目編號 (由 ListView 呼叫, 也稱為 Callback 函式)
        override fun getItemId(position: Int): Long {
            val itemId = position + 0L
            return itemId
        }

        // 取得  某項目用的 Layout (由 ListView 呼叫, 也稱為 Callback 函式)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
// 載入 Layout
            val itemLayout = context.layoutInflater.inflate(R.layout.item_layout, null)
// 尋找 Layout 裡面 View
            val tv_item_image = itemLayout.findViewById<ImageView>(R.id.tv_item_image)
            val tv_item_n = itemLayout.findViewById<TextView>(R.id.tv_item_n)
            val tv_item_e = itemLayout.findViewById<TextView>(R.id.tv_item_e)
            val tv_item_m = itemLayout.findViewById<TextView>(R.id.tv_item_m)

// 顯示資訊
            val drawableId = drawableArrayList.get(position)
            val r2 = stArrayList.get(position)
            tv_item_image.setImageResource(drawableId)
            tv_item_n.setText(r2.name)
            tv_item_e.setText(r2.eng.toString())
            tv_item_m.setText(r2.math.toString())

            return itemLayout
        }

    }
}
