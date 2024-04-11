package com.example.flickinputpractice3

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    val handler = Handler() //timer処理で必要
    var indexNum = 0        //timer処理で必要 現在の経過した時間

    var seikaiCount = 0
    var missCount = 0
    var renzokuMa = false

    var zenkaiYellow  = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.kaishiButton).setOnClickListener {
            forcusEditText()
            loopTextCout()  //ここで文字列の比較を行う。//OKならNextLootを呼ぶ。タイマー処理のみ
        }
    }




    fun loopTextCout() {

        val hantei = ichimojiSyutoku()

        when(hantei){
            "Red" -> {colorRedText()}
            "Yellow" -> {colorYellowText()}
            "White" -> {
                colorWhiteText()
            }
            else -> {}
        }


        val zenHantei = zenbumojiSyutoku()
        when(zenHantei){
            "ZenRed" -> {colorRedText()}
            "ZenWhite" -> {
                if(hantei=="Yellow"){colorYellowText()}
                if(hantei=="Red"){colorRedText()}
            }
            else -> {}
        }

        //間違いカウント処理
        //もし現在がRedなら、それはミス
        //でも連続ミスならカウントしない。
        if( hantei == "Red" ){
            if (renzokuMa) {
            } else {
                missCount += 1
                renzokuMa = true
            }
        }

        if( hantei == "Yellow" ) {
            if (zenHantei == "ZenRed") {
                if (renzokuMa) {
                } else {
                    missCount += 1
                    renzokuMa = true
                }
            }
            if(zenHantei=="ZenWhite"){
                renzokuMa = false
            }
        }
        if( hantei == "White" ){
            if(zenHantei=="ZenWhite") {
                renzokuMa = false
            }
        }


        //正解処理
        val myText = findViewById<EditText>(R.id.editTextText).text.toString()
        val ktText = findViewById<TextView>(R.id.textView1).text.toString()
        if(myText==ktText){
            seikaiCount += ktText.length
            textIrekae()
        }

        nextLoop()
    }





    fun ichimojiSyutoku():String{
        val myText = findViewById<EditText>(R.id.editTextText).text.toString()
        val ktText = findViewById<TextView>(R.id.textView1).text.toString()
        val myCount = myText.length
        val ktCount = ktText.length
        var myHitomoji = ""
        if(myCount==0) {
            myHitomoji = ""
        }else{
            myHitomoji = myText[myCount-1].toString()
        }
        //正解文字列のmyCout個目の文字を取り出そうとするとき、多すぎるとエラーになるので、それを回避
        //０は避けなければいけないので、これで正しい
        var ktHitomoji = ""
        if(myCount==0) {
        }else{
            if(myCount<=ktCount){
                ktHitomoji = ktText[myCount - 1].toString()
            }
        }
        var hantei = ""
        if(myHitomoji==ktHitomoji){
            hantei  = "White"
        }else{
            if(dakutenTokaIchimojinohikaku(myHitomoji,ktHitomoji)){
                hantei = "Yellow"
            }else{
                hantei = "Red"
            }
        }
        return hantei
    }


    fun zenbumojiSyutoku():String{
        val myText = findViewById<EditText>(R.id.editTextText).text.toString()
        val ktText = findViewById<TextView>(R.id.textView1).text.toString()
        val myCount = myText.length
        val ktCount = ktText.length

        var myZenmoji = ""
        if(myCount==0) {
            myZenmoji = ""
        }else{
            myZenmoji = myText.substring(0, myCount-1 )

        }

        //正解文字列のmyCout個目の文字を取り出そうとするとき、多すぎるとエラーになるので、それを回避
        //０は避けなければいけないので、これで正しい
        var ktHZenmoji = ""
        if(myCount==0) {
        }else{
            if(myCount<=ktCount){
                ktHZenmoji = ktText.substring(0, myCount-1)
            }
        }

        var hantei = ""
        if(myZenmoji==ktHZenmoji){
            hantei  = "ZenWhite"
        }else{
            hantei = "ZenRed"
        }

        //判定は白か赤のみ　返ってきたときに白で現在のステイツが黄色のみ、黄色にする。
        //赤ならどんな条件で赤。
        //白判定でも現在が赤なら赤。
        //よし、これでたぶんOK！
        return hantei

    }


    //スタート時にEditTextにフォーカスをあてるだけ。それだけ。あと何か入力されてたら消す
    fun forcusEditText(){
        textSetStart()
        var editText:EditText = findViewById(R.id.editTextText)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText,0)
        findViewById<EditText>(R.id.editTextText).text = Editable.Factory.getInstance().newEditable("")
    }

    //テキスト入れ替え処理　追加用のリストが必要
    val itemList = listOf<String>("のだひろき","もんてでぃおやまがた","でぃふぇんだー","きゅうしゅうだんじ","おおつこうこう","かみましきぐん","ましきまち","ぱたーん","えっとね","だいむりーくらいすらー","みすど","しゃちょう","せんす","ちけっと","しんたいせいはっぴょう","ばいにん","ごーれむ","とらんぺっと","るぱんさんせい","はととしょうねん","えんどれすわるつ")

    //最初にランダムでテキストをセットする。
    fun textSetStart(){
        findViewById<TextView>(R.id.textView1).text= (itemList.random())
        findViewById<TextView>(R.id.textView2).text= (itemList.random())
        findViewById<TextView>(R.id.textView3).text=(itemList.random())
    }

    //テキスト入れ替え処理のみ
    fun textIrekae(){
        findViewById<TextView>(R.id.textView1).text= findViewById<TextView>(R.id.textView2).text
        findViewById<TextView>(R.id.textView2).text= findViewById<TextView>(R.id.textView3).text
        findViewById<TextView>(R.id.textView3).text=(itemList.random())
        findViewById<EditText>(R.id.editTextText).text = Editable.Factory.getInstance().newEditable("")
        colorWhiteText()
        nextLoop()
    }



    //色変え処理のみ
    fun colorRedText(){
        val colorString="#FF0000"
        var editText: EditText = findViewById(R.id.editTextText)
        (editText as TextView).setTextColor(Color.parseColor(colorString))
    }

    fun colorWhiteText(){
        val colorString="#FFFFFF"
        var editText: EditText = findViewById(R.id.editTextText)
        (editText as TextView).setTextColor(Color.parseColor(colorString))
    }

    fun colorYellowText(){
        val colorString="#FFEB3B"
        var editText: EditText = findViewById(R.id.editTextText)
        (editText as TextView).setTextColor(Color.parseColor(colorString))
    }


    //タイマー処理のみ
    fun nextLoop(){
        findViewById<TextView>(R.id.countTimeTextLabel).text=indexNum.toString()
        indexNum += 1 // 次に進める
        if (indexNum >= 100*120) { //二分にする
            findViewById<TextView>(R.id.countTimeTextLabel).text="タイムアップ！"
            findViewById<TextView>(R.id.countTimeTextLabel).text="${seikaiCount
            
            
            
            }文字の入力でした。ミスは${missCount}回です。"


        }else{
            handler.postDelayed({ loopTextCout() }, 10)
        }
    }

    fun dakutenTokaIchimojinohikaku(a:String,b:String):Boolean{
        var c = false

        if (b=="ぁ"){ if (a=="あ"){ c = true } }
        if (b=="ぃ"){ if (a=="い"){ c = true } }
        if (b=="ぅ"){ if (a=="う"){ c = true } }
        if (b=="ぇ"){ if (a=="え"){ c = true } }
        if (b=="ぉ"){ if (a=="お"){ c = true } }

        if (b=="が"){ if (a=="か"){ c = true } }
        if (b=="ぎ"){ if (a=="き"){ c = true } }
        if (b=="ぐ"){ if (a=="く"){ c = true } }
        if (b=="げ"){ if (a=="け"){ c = true } }
        if (b=="ご"){ if (a=="こ"){ c = true } }

        if (b=="ざ"){ if (a=="さ"){ c = true } }
        if (b=="じ"){ if (a=="し"){ c = true } }
        if (b=="ず"){ if (a=="す"){ c = true } }
        if (b=="ぜ"){ if (a=="せ"){ c = true } }
        if (b=="ぞ"){ if (a=="そ"){ c = true } }

        if (b=="だ"){ if (a=="た"){ c = true } }
        if (b=="ぢ"){ if (a=="ち"){ c = true } }
        if (b=="づ"){ if (a=="つ"){ c = true } }
        if (b=="で"){ if (a=="て"){ c = true } }
        if (b=="ど"){ if (a=="と"){ c = true } }

        if (b=="ば"){ if (a=="は" || a=="ぱ"){ c = true } }
        if (b=="び"){ if (a=="ひ"|| a=="ぴ"){ c = true } }
        if (b=="ぶ"){ if (a=="ふ"|| a=="ぷ"){ c = true } }
        if (b=="べ"){ if (a=="へ"|| a=="ぺ"){ c = true } }
        if (b=="ぼ"){ if (a=="ほ"|| a=="ぽ"){ c = true } }

        if (b=="ぱ"){ if (a=="は" || a=="ば"){ c = true } }
        if (b=="ぴ"){ if (a=="ひ"||  a=="び"){ c = true } }
        if (b=="ぷ"){ if (a=="ふ"||  a=="ぶ"){ c = true } }
        if (b=="ぺ"){ if (a=="へ"||  a=="べ"){ c = true } }
        if (b=="ぽ"){ if (a=="ほ"||  a=="ぼ"){ c = true } }
        if (b=="っ"){ if (a=="つ"){ c = true } }
        if (b=="ゃ"){ if (a=="や"){ c = true } }
        if (b=="ゅ"){ if (a=="ゆ"){ c = true } }
        if (b=="ょ"){ if (a=="よ"){ c = true } }
        return c
    }

}