package com.demo.todocrudapp.ui.todo_detail

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.demo.todocrudapp.R
import com.demo.todocrudapp.data.network.model.Response
import com.demo.todocrudapp.data.network.model.TodoDetailTasks
import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.databinding.FragmentDetailTaskBinding
import com.demo.todocrudapp.ui.home.TodoViewModel
import kotlinx.coroutines.launch


class DetailTaskFragment : Fragment() {
    private var banner: TodoTasks?=null
    private var bannerTitle: String = ""
    private var todo_detail_binding: FragmentDetailTaskBinding? = null
    private val bindingdetails get() = todo_detail_binding!!
    private val viewModel: TodoViewModel by activityViewModels()
    private var detailsTodo: TodoDetailTasks? = null
    private var styles_spinner_data: ArrayList<String> = ArrayList()
    private var fontStyle: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun iTextfont_spinner() {
        styles_spinner_data.clear()
        styles_spinner_data.add(/*getString(R.string.select_gender)*/"Select A style")
        styles_spinner_data.add("Bold")
        styles_spinner_data.add("Italics")
        styles_spinner_data.add("Underline")
        val spinnerAdapter =
            SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, styles_spinner_data)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        bindingdetails.fontSpinner.adapter = spinnerAdapter
        bindingdetails.fontSpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    when (position) {
                        1 -> {
                            fontStyle = 1
                        }
                        2 -> {
                            fontStyle = 2
                        }
                        3->{
                            fontStyle = 3
                        }
                    }
                } else {
                    fontStyle = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })
    }
    private fun setToolbar() {
        bindingdetails.todoAddFragmentToolbar.title = "Details Fragment"
        bindingdetails.todoAddFragmentToolbar.setNavigationIcon(R.drawable.ic_back_icon)
        bindingdetails.todoAddFragmentToolbar.setNavigationOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })

    }
    private fun addTodoDetails(details:TodoTasks){

        bindingdetails.apply {
            if (txtEnterTodoDetails.text.toString().isEmpty()) {
                txtEnterTodoDetails.text = null
            } else {
                val todoDetails = txtEnterTodoDetails.text.toString()
                viewModel.insertTodoDetails(details.id,details.name,todoDetails,fontStyle)

                findNavController().navigateUp()
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todo_detail_binding = FragmentDetailTaskBinding.inflate(inflater, container, false)

        setToolbar()

        if (arguments != null) {
            if (arguments?.getString("title") != null) {
                bannerTitle = arguments?.getString("title").toString().replace("\n", " ")
                banner = arguments?.getSerializable("data") as TodoTasks?
                Log.d("dataksksksOnly","$banner")
               // addTodoDetails(banner!!)
            }
        }

        bindingdetails.titleTv.text = banner?.name
        iTextfont_spinner()

         viewModel.getTodoDetails(banner?.id!!)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todosDetails.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            detailsTodo=response.data

                            when (detailsTodo?.type) {
                                1 -> {
                                    bindingdetails.titleTvDetails.text = detailsTodo?.titleDetails?:""
                                    bindingdetails.titleTvDetails.setTypeface(null, Typeface.BOLD)
                                    bindingdetails.titleTvDetails.setPaintFlags(0)

                                }
                                2 -> {
                                    bindingdetails.titleTvDetails.text = detailsTodo?.titleDetails?:""
                                    bindingdetails.titleTvDetails.setTypeface(null, Typeface.ITALIC)
                                    bindingdetails.titleTvDetails.setPaintFlags(0)

                                }
                                3 ->{
                                    bindingdetails.titleTvDetails.text = detailsTodo?.titleDetails?:""
                                    bindingdetails.titleTvDetails.paintFlags = bindingdetails.titleTvDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                                }
                                else -> { // Note the block
                                    bindingdetails.titleTvDetails.text = detailsTodo?.titleDetails?:""
                                    bindingdetails.titleTvDetails.setTypeface(null, Typeface.NORMAL)
                                    bindingdetails.titleTvDetails.setPaintFlags(0)

                                }
                            }
                            //bindingdetails.titleTvDetails.text = detailsTodo?.titleDetails?:""
                        }
                        else -> {}
                    }
                }
            }
        }




        bindingdetails.btnSaveTodoDetails.setOnClickListener {
            addTodoDetails(banner!!)}


        return bindingdetails.root
        // Inflate the layout for this fragment
    }
}