/*
 * Copyright 2018 Readium Foundation. All rights reserved.
 * Licensed to the Readium Foundation under one or more contributor license agreements.
 * Use of this source code is governed by a BSD-style license which is detailed in the
 * LICENSE file present in the project repository where this source code is maintained.
 */

package org.readium.r2.testapp

import android.app.Activity
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.io.ByteArrayInputStream


open class BooksAdapter(private val activity: Activity, private var books: MutableList<Book>, private val server: String, private var itemListener: RecyclerViewClickListener) : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksAdapter.ViewHolder {
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.item_recycle_opds, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: BooksAdapter.ViewHolder, position: Int) {

        val book = books[position]

        viewHolder.textView.text = book.title

        viewHolder.imageView.setImageResource(R.drawable.cover)

        book.cover?.let {
            val arrayInputStream = ByteArrayInputStream(it)
            val bitmap = BitmapFactory.decodeStream(arrayInputStream)
            viewHolder.imageView.setImageBitmap(bitmap)
        } ?: run {
            book.coverLink?.let {
                val baseUrl = server + "/" + book.fileName + it
                Picasso.with(activity).load(baseUrl).into(viewHolder.imageView)
            }
        }

        viewHolder.itemView.setOnClickListener({ v ->
            //get the position of the image which is clicked
            itemListener.recyclerViewListClicked(v, position)
        })

        viewHolder.itemView.setOnLongClickListener({ v ->
            //get the position of the image which is clicked
            itemListener.recyclerViewListLongClicked(v, position)
            true
        })


    }

    override fun getItemCount(): Int {
        return books.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById<View>(R.id.titleTextView) as TextView
        val imageView: ImageView = view.findViewById(R.id.coverImageView) as ImageView

    }

    interface RecyclerViewClickListener {

        //this is method to handle the event when clicked on the image in Recyclerview
        fun recyclerViewListClicked(v: View, position: Int)

        fun recyclerViewListLongClicked(v: View, position: Int)
    }

}

