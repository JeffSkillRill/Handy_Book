package uz.ictschool.handybook.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import farrukh.remotely.adapter.CommentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.ictschool.handybook.R
import uz.ictschool.handybook.api.APIClient
import uz.ictschool.handybook.api.APIService
import uz.ictschool.handybook.data.Book
import uz.ictschool.handybook.data.Comment
import uz.ictschool.handybook.data.CommentData
import uz.ictschool.handybook.databinding.FragmentCommentBinding
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Book? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Book

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCommentBinding.inflate(inflater,container,false)

        val api = APIClient.getInstance().create(APIService::class.java)

        api.getAllComments(param1!!.id).enqueue(object :Callback<List<Comment>>{
            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                var adapter = CommentAdapter(response.body()!!.toMutableList())
                var layoutManager = GridLayoutManager(requireContext(),1,
                    LinearLayoutManager.VERTICAL,false)

                binding.commentsRv.adapter = adapter
                binding.commentsRv.layoutManager = layoutManager
                Log.d("TAG", "onResponse:${response.body()} ")

            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })

        binding.addNewComment.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main,RatingFragment.newInstance(param1!!)).commit()
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Book) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)

                }
            }
    }
}