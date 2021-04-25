package loodos.droid.bitcointicker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.util.ImageLoader
import loodos.droid.bitcointicker.util.UIHelper
import loodos.droid.bitcointicker.util.extensions.dollarString
import loodos.droid.bitcointicker.util.extensions.emptyIfNull
import kotlinx.android.synthetic.main.item_coins_list.view.*
import loodos.droid.bitcointicker.R
import kotlin.collections.ArrayList

/**
 * listener for add to favourite and item click
 */
interface OnItemClickCallback {
    fun onItemClick(symbol: String, id: String)
    fun onFavouriteClicked(symbol: String)
}

class CoinsListAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<CoinsListAdapter.CoinsListViewHolder>() {

    private val coinsList: ArrayList<CoinsListEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        return CoinsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_coins_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        holder.bind(coinsList[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = coinsList.size

    fun updateList(list: List<CoinsListEntity>) {
        this.coinsList.clear()
        this.coinsList.addAll(list)
        notifyDataSetChanged()
    }

    class CoinsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // bind the data with the list view item
        fun bind(model: CoinsListEntity, onItemClickCallback: OnItemClickCallback) {
            itemView.coinsItemSymbolTextView.text = model.symbol
            itemView.coinsItemNameTextView.text = model.name.emptyIfNull()
            itemView.coinsItemPriceTextView.text = model.price.dollarString()

            UIHelper.showChangePercent(itemView.coinsItemChangeTextView, model.changePercent)

            ImageLoader.loadImage(itemView.coinsItemImageView, model.image ?: "")

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(
                    model.symbol,
                    model.id ?: model.symbol
                )
            }
        }
    }
}