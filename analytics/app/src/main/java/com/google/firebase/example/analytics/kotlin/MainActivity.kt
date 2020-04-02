package com.google.firebase.example.analytics.kotlin

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.example.analytics.R
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enhancedEcommerce()
    }

    fun enhancedEcommerce() {
        val analytics = Firebase.analytics

        // [START create_items]
        val itemJeggings = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 9.99)
        }

        val itemBoots = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_456")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "boots")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "shoes")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "brown")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 24.99)
        }

        val itemSocks = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_789")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "ankle_socks")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "socks")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "red")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 5.99)
        }
        // [END create_items]

        // [START view_item_list]
        val itemJeggingsWithIndex = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 1)
        }
        val itemBootsWithIndex = Bundle(itemBoots).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 2)
        }
        val itemSocksWithIndex = Bundle(itemSocks).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 3)
        }
        val viewItemListParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    arrayOf<Parcelable>(itemJeggingsWithIndex, itemBootsWithIndex, itemSocksWithIndex))
        }

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, viewItemListParams)
        // [END view_item_list]

        // [START select_item]
        val selectItemParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    arrayOf<Parcelable>(itemJeggings))
        }

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams)
        // [END select_item]

        // [START view_product_details]
        val viewItemParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 9.99)
            putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    arrayOf<Parcelable>(itemJeggings))
        }

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, viewItemParams)
        // [END view_product_details]

        // [START add_to_cart_wishlist]
        val itemJeggingsWishlist = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }
        val addToWishlistParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 2 * 9.99)
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsWishlist))
        }

        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, addToWishlistParams)
        // [END add_to_cart_wishlist]

        // [START view_cart]
        val itemJeggingsCart = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }
        val itemBootsCart = Bundle(itemBoots).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }
        val viewCartParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 2 * 9.99 + 1 * 24.99)
            putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                    arrayOf<Parcelable>(itemJeggingsCart, itemBootsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART, viewCartParams)
        // [END view_cart]

        // [START remove_from_cart]
        val removeCartParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 1 * 24.99)
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemBootsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, removeCartParams)
        // [END remove_from_cart]

        // [START start_checkout]
        val beginCheckoutParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
            putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, beginCheckoutParams)
        // [END start_checkout]

        // [START add_shipping]
        val addShippingParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
            putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            putString(FirebaseAnalytics.Param.SHIPPING_TIER, "Ground")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO, addShippingParams)
        // [END add_shipping]

        // [START add_payment]
        val addPaymentParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
            putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            putString(FirebaseAnalytics.Param.PAYMENT_TYPE, "Visa")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, addPaymentParams)
        // [END add_payment]

        // [START log_purchase]
        val purchaseParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            putString(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
            putDouble(FirebaseAnalytics.Param.TAX, 2.58)
            putDouble(FirebaseAnalytics.Param.SHIPPING, 5.34)
            putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))
        }

        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, purchaseParams)
        // [END log_purchase]

        // [START log_refund]
        val refundParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            putString(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            putDouble(FirebaseAnalytics.Param.VALUE, 9.99)

            // (Optional) for partial refunds, define the item ID and quantity of refunded items
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)

            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))
        }

        analytics.logEvent(FirebaseAnalytics.Event.REFUND, refundParams)
        // [END log_refund]

        // [START apply_promo]
        val promoParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.PROMOTION_ID, "SUMMER_FUN")
            putString(FirebaseAnalytics.Param.PROMOTION_NAME, "Summer Sale")
            putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg")
            putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1")
            putString(FirebaseAnalytics.Param.LOCATION_ID, "HERO_BANNER")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))
        }

        // Promotion displayed
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams)

        // Promotion selected
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams)
        // [END apply_promo]
    }
}
