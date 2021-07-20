package com.example.a2021summer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2021summer.databinding.ActivityMainBinding
import com.example.a2021summer.databinding.ActivitySubBinding
import kotlin.concurrent.thread

class SubActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySubBinding
    lateinit var passData: Array<String>
    lateinit var selected: Array<Int>
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        passData = intent.getSerializableExtra("passdata") as Array<String> //1: menutable/ 2: info/ 3: name
        viewBinding.shopTitle.text = passData[2]
        viewBinding.shopInfo.text = passData[1]
        var jsonmanager = JSONManager()
        var subContext = this
        thread(start=true){
            var tmp = jsonmanager.loadMenuList(passData[0])
            var menuAdapter = MenuListAdapter(subContext,tmp)
            selected = Array(tmp.size,{i->0})
            runOnUiThread{
                viewBinding.menulist.adapter = menuAdapter
                var layout = LinearLayoutManager(subContext)
                viewBinding.menulist.layoutManager = layout
                viewBinding.menulist.setHasFixedSize(true)
                menuAdapter.notifyDataSetChanged()
            }
        }
        viewBinding.goToCart.setOnClickListener{
            var tmplist = mutableListOf<Int>()
            for (i in 0..selected.size-1){
                if(selected[i] == 1){
                    tmplist.add(i+1)
                }
            }
            if(tmplist.size == 0){
                Toast.makeText(this,"메뉴를 골라주세요",Toast.LENGTH_SHORT)
            } else {
                var intent = Intent(this,OrderActivity::class.java)
                intent.putExtra("name",passData[2])
                intent.putExtra("selectedmenu",tmplist.toIntArray())
                startActivity(intent)
            }

        }

    }

    override fun onBackPressed(){
        finish()
    }
}