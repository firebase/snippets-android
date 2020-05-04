package com.google.firebase.example.analytics.kotlin

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
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

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            param(FirebaseAnalytics.Param.ITEMS,
                    arrayOf(itemJeggingsWithIndex, itemBootsWithIndex, itemSocksWithIndex))
        }
        // [END view_item_list]

        // [START select_item]
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
        // [END select_item]

        // [START view_product_details]
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 9.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
        // [END view_product_details]

        // [START add_to_cart_wishlist]
        val itemJeggingsWishlist = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }

        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 2 * 9.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsWishlist))
        }
        // [END add_to_cart_wishlist]

        // [START view_cart]
        val itemJeggingsCart = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }
        val itemBootsCart = Bundle(itemBoots).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 2 * 9.99 + 1 * 24.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart, itemBootsCart))
        }
        // [END view_cart]

        // [START remove_from_cart]
        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 1 * 24.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemBootsCart))
        }
        // [END remove_from_cart]

        // [START start_checkout]
        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END start_checkout]

        // [START add_shipping]
        analytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.SHIPPING_TIER, "Ground")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END add_shipping]

        // [START add_payment]
        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.PAYMENT_TYPE, "Visa")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END add_payment]

        // [START log_purchase]
        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            param(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.TAX, 2.58)
            param(FirebaseAnalytics.Param.SHIPPING, 5.34)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END log_purchase]

        // [START log_refund]
        analytics.logEvent(FirebaseAnalytics.Event.REFUND) {
            param(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            param(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 9.99)

            // (Optional) for partial refunds, define the item ID and quantity of refunded items
            param(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            param(FirebaseAnalytics.Param.QUANTITY, 1)

            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
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
