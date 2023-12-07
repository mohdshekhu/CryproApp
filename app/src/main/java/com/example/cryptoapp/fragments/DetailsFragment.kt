package com.example.cryptoapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.database.MyDbHelper
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.modals.CryptoCurrency

class DetailsFragment : Fragment() {

    lateinit var binding : FragmentDetailsBinding

    private val args : DetailsFragmentArgs by navArgs()
    private var isAvailable : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val data : CryptoCurrency? = args.detailsData
        setUpDetails(data)
        loadChart(data)
        setButtonOnClick(data)
        checkWatchList(data!!.id)

        binding.backStackButton.setOnClickListener(){
            requireActivity().onBackPressed()
        }

        binding.addWatchlistButton.setOnClickListener(){

//            binding.addWatchlistButton.setImageDrawable(resources.getDrawable(R.drawable.ic_star))
            var db = MyDbHelper(requireContext())
            if (isAvailable){
                isAvailable = false
                binding.addWatchlistButton.setImageDrawable(resources.getDrawable(R.drawable.ic_star_outline))
                db.delete(data.id)
            }else{
                isAvailable = true
                binding.addWatchlistButton.setImageDrawable(resources.getDrawable(R.drawable.ic_star))
                db.addFav(data!!.id, true)
            }



        }

        return binding.root
    }

    private fun checkWatchList(id:Int) {
        val helper = MyDbHelper(requireContext())
        Log.d("checkLogs","helper Created")
        isAvailable = helper.isAvail(id)
        Log.d("checkLogs","IsAvailable Checked")
        if (isAvailable){
            binding.addWatchlistButton.setImageDrawable(resources.getDrawable(R.drawable.ic_star))
        }

    }


    private fun setButtonOnClick(data: CryptoCurrency?) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMin = binding.button5

        val clickListener = View.OnClickListener {
            when(it.id){
                oneMonth.id   -> loadChartData(it,"15",data,oneDay,fifteenMin,oneWeek,fourHour,oneHour)
                oneWeek.id    -> loadChartData(it, "1H",data, oneDay,oneMonth,fifteenMin,fourHour,oneHour)
                oneDay.id     -> loadChartData(it,"4H",data,fifteenMin,oneMonth,oneWeek,fourHour,oneHour)
                fourHour.id   -> loadChartData(it, "D",data,oneDay,oneMonth,oneWeek,fifteenMin,oneHour)
                oneHour.id    -> loadChartData(it,"W",data,oneDay,oneMonth,oneWeek,fourHour,fifteenMin)
                fifteenMin.id -> loadChartData(it,"M" , data,oneDay,oneMonth,oneWeek,fourHour,oneHour)
            }
        }
        fifteenMin.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        data: CryptoCurrency?,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {

        it!!.setBackgroundResource(R.drawable.active_button)
        disableBtn(oneDay,oneWeek,oneHour,oneMonth,fourHour)

        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl(

            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"+ data!!.symbol+ "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=" +
                    "1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source" +
                    "=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    private fun disableBtn(oneDay: AppCompatButton, oneWeek: AppCompatButton,
                           oneHour: AppCompatButton, oneMonth: AppCompatButton, fourHour: AppCompatButton) {

        oneDay.background = null
        oneHour.background = null
        oneWeek.background = null
        oneMonth.background = null
        fourHour.background = null
    }

    private fun loadChart(currency: CryptoCurrency?) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol"+ currency!!.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpDetails(data: CryptoCurrency?) {
        binding.detailSymbolTextView.text = data!!.symbol
        binding.detailPriceTextView.text = "$${String.format("%.02f",data.quotes[0].price)}%"

        Glide.with(requireContext()).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+ data.id+".png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.loading)).into(binding.detailImageView)

        if (data.quotes[0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = "${String.format("%.02f",data.quotes[0].percentChange24h)}%"
        }

    }
}