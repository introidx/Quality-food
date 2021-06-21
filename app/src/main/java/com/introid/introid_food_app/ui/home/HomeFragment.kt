package com.introid.introid_food_app.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.introid.introid_food_app.R
import com.google.firebase.ktx.Firebase
import com.introid.introid_food_app.adapters.AllMenuAdapter
import com.introid.introid_food_app.adapters.PopularAdapter
import com.introid.introid_food_app.adapters.RecommendedAdapter
import com.introid.introid_food_app.adapters.ServiceAdapter
import com.introid.introid_food_app.localDB.room.database.FoodDatabase
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.models.Services
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val TAG = "HomeFragment"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var firebaseDbPopular: FirebaseFirestore
    private lateinit var firebaseDbRecommended: FirebaseFirestore
    private lateinit var firebaseDbAllMenuItems: FirebaseFirestore
    private lateinit var popularList: MutableList<Food>
    private lateinit var recommendedList: List<Food>
    private lateinit var allItemList: MutableList<Food>
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var allMenuAdapter: AllMenuAdapter
    private lateinit var serviceAdapter: ServiceAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[FoodViewModels::class.java]

        popularList = mutableListOf()
        recommendedList = mutableListOf()
        allItemList = mutableListOf()

        popularAdapter = PopularAdapter(this, popularList)
        recommendedAdapter = RecommendedAdapter(this, recommendedList)
        allMenuAdapter = AllMenuAdapter(this, allItemList)

        firebaseDbPopular = FirebaseFirestore.getInstance()
        firebaseDbRecommended = FirebaseFirestore.getInstance()
        firebaseDbAllMenuItems = FirebaseFirestore.getInstance()

        val serviceList = mutableListOf(
            Services(R.drawable.ic_burger, "Burger"),
            Services(R.drawable.ic_pizza, "Pizza"),
            Services(R.drawable.ic_chinese, "Chinese"),
            Services(R.drawable.ic_fried_chicken, "Fried C"),
            Services(R.drawable.ic_drink, "Drinks")
        )

        serviceAdapter = ServiceAdapter(this, serviceList)
        popular_recycler.adapter = serviceAdapter


        viewModel.recommendedFood.observe(viewLifecycleOwner , Observer {
            recommendedList = it
        })
/*        val recommendedReference = firebaseDbRecommended
            .collection("recommended")

        val allMenuItemsReference = firebaseDbAllMenuItems
            .collection("recommended")*/

//        popularReference.addSnapshotListener{snapshot, exception ->
//            if (exception != null || snapshot == null){
//                Log.d(TAG, "onCreate: ", exception)
//                return@addSnapshotListener
//            }
//            val postList = snapshot.toObjects(Food::class.java)
//            popularList.clear()
//            popularList.addAll(postList)
//            popularAdapter.notifyDataSetChanged()
//
//        }

/*        recommendedReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.d(TAG, "onCreate: ", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Food::class.java)
            recommendedList.clear()
            recommendedList.addAll(postList)
            recommendedAdapter.notifyDataSetChanged()

        }

        allMenuItemsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.d(TAG, "onCreate: ", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Food::class.java)
            allItemList.clear()
            allItemList.addAll(postList)
            allMenuAdapter.notifyDataSetChanged()

        }*/
//        popular_recycler.adapter = popularAdapter
        recommended_recycler.adapter = recommendedAdapter
        all_menu_recycler.adapter = allMenuAdapter
//
//        popularAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("food", it)
//            }
//            findNavController().navigate(
//                    R.id.action_homeFragment_to_productDetailFragment,
//                    bundle
//            )
//        }
        recommendedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("food", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailFragment,
                bundle
            )
        }
        allMenuAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("food", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailFragment,
                bundle
            )
        }

        iv_go_to_cart.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_checkoutPageFragment
            )
        }
    }
    
}
