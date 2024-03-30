package com.bank.bcamobiie.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.datadummy.DataTerm

class DataTermAdapter(private val dataTerms: List<DataTerm>) : RecyclerView.Adapter<DataTermAdapter.DataTermViewHolder>() {

    inner class DataTermViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.descTerm)
        val textViewNo: TextView = itemView.findViewById(R.id.noTerm)
        val textViewNoDetail: TextView = itemView.findViewById(R.id.noDetail)
        val textViewDetailTerm: TextView = itemView.findViewById(R.id.detailTerm)
        val textViewNoDetailB: TextView = itemView.findViewById(R.id.noDetailB)
        val textViewDetailTermB: TextView = itemView.findViewById(R.id.detailTermB)
        val textViewNoDetailC: TextView = itemView.findViewById(R.id.noDetailC)
        val textViewDetailTermC: TextView = itemView.findViewById(R.id.detailTermC)
        val textViewNoDetailD: TextView = itemView.findViewById(R.id.noDetailD)
        val textViewDetailTermD: TextView = itemView.findViewById(R.id.detailTermD)
        val textViewNoDetailE: TextView = itemView.findViewById(R.id.noDetailE)
        val textViewDetailTermE: TextView = itemView.findViewById(R.id.detailTermE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataTermViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_term, parent, false)
        return DataTermViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataTermViewHolder, position: Int) {
        val currentDataTerm = dataTerms[position]
        if (currentDataTerm.title.isEmpty()) {
            holder.textViewTitle.visibility = View.GONE
        } else {
            holder.textViewTitle.visibility = View.VISIBLE
            holder.textViewTitle.text = Html.fromHtml(currentDataTerm.title, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.text.isEmpty()) {
            holder.textViewDescription.visibility = View.GONE
        } else {
            holder.textViewDescription.visibility = View.VISIBLE
            holder.textViewDescription.text = Html.fromHtml(currentDataTerm.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetail.isEmpty()) {
            holder.textViewNoDetail.visibility = View.GONE
        } else {
            holder.textViewNoDetail.visibility = View.VISIBLE
            holder.textViewNoDetail.text = Html.fromHtml(currentDataTerm.noDetail, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.detail.isEmpty()) {
            holder.textViewDetailTerm.visibility = View.GONE
        } else {
            holder.textViewDetailTerm.visibility = View.VISIBLE
            holder.textViewDetailTerm.text = Html.fromHtml(currentDataTerm.detail, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.detailB.isEmpty()) {
            holder.textViewDetailTermB.visibility = View.GONE
        } else {
            holder.textViewDetailTermB.visibility = View.VISIBLE
            holder.textViewDetailTermB.text = Html.fromHtml(currentDataTerm.detailB, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.detailC.isEmpty()) {
            holder.textViewDetailTermC.visibility = View.GONE
        } else {
            holder.textViewDetailTermC.visibility = View.VISIBLE
            holder.textViewDetailTermC.text = Html.fromHtml(currentDataTerm.detailC, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.detailD.isEmpty()) {
            holder.textViewDetailTermD.visibility = View.GONE
        } else {
            holder.textViewDetailTermD.visibility = View.VISIBLE
            holder.textViewDetailTermD.text = Html.fromHtml(currentDataTerm.detailD, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.detailE.isEmpty()) {
            holder.textViewDetailTermE.visibility = View.GONE
        } else {
            holder.textViewDetailTermE.visibility = View.VISIBLE
            holder.textViewDetailTermE.text = Html.fromHtml(currentDataTerm.detailE, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetail.isEmpty()) {
            holder.textViewNoDetail.visibility = View.GONE
        } else {
            holder.textViewNoDetail.visibility = View.VISIBLE
            holder.textViewNoDetail.text = Html.fromHtml(currentDataTerm.noDetail, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetailB.isEmpty()) {
            holder.textViewNoDetailB.visibility = View.GONE
        } else {
            holder.textViewNoDetailB.visibility = View.VISIBLE
            holder.textViewNoDetailB.text = Html.fromHtml(currentDataTerm.noDetailB, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetailC.isEmpty()) {
            holder.textViewNoDetailC.visibility = View.GONE
        } else {
            holder.textViewNoDetailC.visibility = View.VISIBLE
            holder.textViewNoDetailC.text = Html.fromHtml(currentDataTerm.noDetailC, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetailD.isEmpty()) {
            holder.textViewNoDetailD.visibility = View.GONE
        } else {
            holder.textViewNoDetailD.visibility = View.VISIBLE
            holder.textViewNoDetailD.text = Html.fromHtml(currentDataTerm.noDetailD, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        if (currentDataTerm.noDetailE.isEmpty()) {
            holder.textViewNoDetailE.visibility = View.GONE
        } else {
            holder.textViewNoDetailE.visibility = View.VISIBLE
            holder.textViewNoDetailE.text = Html.fromHtml(currentDataTerm.noDetailE, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        holder.textViewNo.text = Html.fromHtml(currentDataTerm.no, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun getItemCount() = dataTerms.size
}