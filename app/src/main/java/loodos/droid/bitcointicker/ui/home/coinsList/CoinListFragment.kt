package loodos.droid.bitcointicker.ui.home.coinsList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list.*
import loodos.droid.bitcointicker.adapters.CoinsListAdapter
import loodos.droid.bitcointicker.adapters.OnItemClickCallback
import loodos.droid.bitcointicker.core.common.MainNavigationFragment
import loodos.droid.bitcointicker.databinding.FragmentListBinding
import loodos.droid.bitcointicker.ui.projectProfile.ProjectProfileActivity
import loodos.droid.bitcointicker.util.Constants
import loodos.droid.bitcointicker.util.extensions.doOnChange
import java.util.*

@AndroidEntryPoint
class CoinListFragment : MainNavigationFragment(), OnItemClickCallback {

    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var binding: FragmentListBinding
    private var coinsListAdapter = CoinsListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@CoinListFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        viewModel.loadCoinsFromApi()
    }

    override fun initializeViews() {
        coinsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinsListAdapter
        }

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                coinsListAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun observeViewModel() {
        viewModel.isLoading.doOnChange(this) {
            coinsListLoading.visibility =
                if (viewModel.isListEmpty() && it) View.VISIBLE else View.GONE

            if (it) {
                coinsListErrorView.visibility = View.GONE
            }
        }

        viewModel.coinsListData.doOnChange(this) {
            coinsListAdapter.updateList(it)

            coinsListErrorView.visibility =
                if (viewModel.isListEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onItemClick(symbol: String, id: String) {
        requireActivity().run {
            startActivity(
                Intent(this, ProjectProfileActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_SYMBOL, symbol)
                        putExtra(Constants.EXTRA_SYMBOL_ID, id)
                    }
            )
        }

    }

    override fun onFavouriteClicked(symbol: String) {}
}