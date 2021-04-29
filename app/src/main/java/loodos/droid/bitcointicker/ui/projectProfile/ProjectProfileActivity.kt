package loodos.droid.bitcointicker.ui.projectProfile

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import loodos.droid.bitcointicker.util.ChartHelper
import loodos.droid.bitcointicker.util.Constants
import loodos.droid.bitcointicker.util.ImageLoader
import loodos.droid.bitcointicker.util.UIHelper
import loodos.droid.bitcointicker.util.extensions.doOnChange
import loodos.droid.bitcointicker.util.extensions.dollarString
import loodos.droid.bitcointicker.util.extensions.emptyIfNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_project_profile.*
import kotlinx.android.synthetic.main.activity_project_profile.view.*
import loodos.droid.bitcointicker.R
import loodos.droid.bitcointicker.adapters.OnItemClickCallback
import loodos.droid.bitcointicker.core.common.BaseActivity
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.databinding.ActivityProjectProfileBinding
import loodos.droid.bitcointicker.util.Constants.DEFAULT_LIST_OF_DAYS

@AndroidEntryPoint
class ProjectProfileActivity : BaseActivity(), OnItemClickCallback {

    private val viewModel: ProjectProfileViewModel by viewModels()
    private lateinit var binding: ActivityProjectProfileBinding

    private var symbol: String? = null
    private var symbolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_profile)
        binding.apply {
            lifecycleOwner = this@ProjectProfileActivity
            viewModel = this@ProjectProfileActivity.viewModel
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL) == true) {
            symbol = intent?.getStringExtra(Constants.EXTRA_SYMBOL)
        }

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL_ID) == true) {
            symbolId = intent?.getStringExtra(Constants.EXTRA_SYMBOL_ID)
        }

        supportActionBar?.title = symbol ?: ""
        observeViewModel()

        changeChartTitle(DEFAULT_LIST_OF_DAYS)
        seekBar.progress = DEFAULT_LIST_OF_DAYS
        viewModel.historicalData(symbolId, DEFAULT_LIST_OF_DAYS)
    }

    private fun observeViewModel() {
        symbol?.let {
            viewModel.favoriteCoinsList.doOnChange(this) {
                if (it.isEmpty()) {
                    viewModel.isFavouritesEmpty(true)
                }
            }
            viewModel.projectBySymbol(it).doOnChange(this) { project ->
                populateViews(project)
            }

            viewModel.historicalData.doOnChange(this) { historicalPriceList ->
                ChartHelper.displayHistoricalLineChart(lineChart, it, historicalPriceList)
            }

            viewModel.dataError.doOnChange(this) { error ->
                if (error) showToast(getString(R.string.historical_data_error))
            }

            viewModel.favouriteStock.doOnChange(this) {
                it?.let {
                    showToast(
                        getString(if (it.isFavourite) R.string.added_to_favourite else R.string.removed_to_favourite).format(
                            it.symbol
                        )
                    )
                }
            }
        }
    }

    private fun populateViews(project: CoinsListEntity) {
        coinItemSymbolTextView.text = project.symbol
        coinItemNameTextView.text = project.name.emptyIfNull()
        coinItemPriceTextView.text = project.price.dollarString()
        UIHelper.showChangePercent(coinItemChangeTextView, project.changePercent)
        ImageLoader.loadImage(coinItemImageView, project.image ?: "")

        favouriteImageView.setImageResource(
            if (project.isFavourite) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )

        favouriteImageView.setOnClickListener {
            viewModel.updateFavouriteStatus(project.symbol)
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                changeChartTitle(p1)
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.progress.let {
                    if (it!! > 1)
                        viewModel.historicalData(symbolId, it)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadCoinsFromApi()
            showToast(getString(R.string.refresh_msg))
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun changeChartTitle(format: Int) {
        lineChartTitle.text = getString(R.string.line_chart_title).format(format)
    }

    override fun onItemClick(symbol: String, id: String) {}

    override fun onFavouriteClicked(symbol: String) {
        viewModel.updateFavouriteStatus(symbol)
    }
}