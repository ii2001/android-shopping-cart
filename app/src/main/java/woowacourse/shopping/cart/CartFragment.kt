package woowacourse.shopping.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import woowacourse.shopping.databinding.FragmentCartListBinding
import woowacourse.shopping.listener.OnClickCartItemCounter
import woowacourse.shopping.model.CartItem

class CartFragment : Fragment(), OnClickCartItemCounter {
    private val viewModel: CartViewModel by viewModels()
    private val adapter: CartItemRecyclerViewAdapter by lazy {
        CartItemRecyclerViewAdapter(
            viewModel.itemsInShoppingCartPage.value ?: emptyList(),
            onClickCartItemCounter = this,
            onClick = { deleteItemId ->
                viewModel.deleteItem(deleteItemId)
            },
        )
    }
    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartListBinding.inflate(inflater)
        binding.cartList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.productDetailToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.itemsInShoppingCartPage.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        viewModel.cartItem.observe(viewLifecycleOwner) {
            adapter.updateCartItems(it)
        }
    }

    override fun increaseQuantity(cartItem: CartItem) {
        viewModel.increaseQuantity(cartItem.productId)
    }

    override fun decreaseQuantity(cartItem: CartItem) {
        viewModel.decreaseQuantity(cartItem.productId)
    }
}
