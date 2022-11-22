package com.example.fintrack.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.R
import com.example.fintrack.databinding.CategoryBinding
import com.example.fintrack.service.model.mt.Category
import com.example.fintrack.service.model.mt.CategoryType
import com.example.fintrack.view.ui.utils.cutText

class CategoryListAdapter(
    categories: List<Category>,
    private val deleteOnClickListener: DeleteOnClickListener,
    private val editOnClickListener: EditOnClickListener
) : RecyclerView.Adapter<CategoryListAdapter.OperationCategoryViewHolder>() {
    private var categoriesList: MutableList<Category> = categories.toMutableList()

    inner class OperationCategoryViewHolder(val binding: CategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DeleteOnClickListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) {
            clickListener(position)
        }
    }

    class EditOnClickListener(val clickListener: (category: Category) -> Unit) {
        fun onClick(category: Category) {
            clickListener(category)
        }
    }

    internal fun deleteCategory(categoryId: Int) {
        val position = categoriesList.map {
            it.id
        }.indexOf(categoryId)

        if (position != -1) {
            categoriesList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationCategoryViewHolder {
        val binding = CategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OperationCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationCategoryViewHolder, position: Int) {
        val currentCategory = categoriesList[position]
        val color =
            if (currentCategory.type == CategoryType.OUTCOME) R.color.main_red else R.color.main_green

        holder.binding.apply {
            id.text = currentCategory.id.toString()
            textName.text = currentCategory.name.cutText(40)
            if (currentCategory.isInternal) {
                imageInternal.visibility = View.VISIBLE
            } else {
                imageInternal.visibility = View.INVISIBLE
            }
            imageType.setColorFilter(ContextCompat.getColor(imageType.context, color))
            buttonDelete.setOnClickListener {
                deleteOnClickListener.onClick(currentCategory.id)
            }
            buttonEdit.setOnClickListener {
                editOnClickListener.onClick(currentCategory)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }
}