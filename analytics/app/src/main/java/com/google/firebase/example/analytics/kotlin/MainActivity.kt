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
        val itemJeggings = Bundle()
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings")
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants")
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black")
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
        itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, 9.99)

        val itemBoots = Bundle()
        itemBoots.putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_456")
        itemBoots.putString(FirebaseAnalytics.Param.ITEM_NAME, "boots")
        itemBoots.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "shoes")
        itemBoots.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "brown")
        itemBoots.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
        itemBoots.putDouble(FirebaseAnalytics.Param.PRICE, 24.99)

        val itemSocks = Bundle()
        itemSocks.putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_789")
        itemSocks.putString(FirebaseAnalytics.Param.ITEM_NAME, "ankle_socks")
        itemSocks.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "socks")
        itemSocks.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "red")
        itemSocks.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
        itemSocks.putDouble(FirebaseAnalytics.Param.PRICE, 5.99)
        // [END create_items]

        // [START view_item_list]
        val itemJeggingsWithIndex = Bundle(itemJeggings)
        itemJeggingsWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 1)
        val itemBootsWithIndex = Bundle(itemJeggings)
        itemBootsWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 2)
        val itemSocksWithIndex = Bundle(itemJeggings)
        itemSocksWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 3)
        val viewItemListParams = Bundle()
        viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
        viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
        viewItemListParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsWithIndex, itemBootsWithIndex, itemSocksWithIndex))

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, viewItemListParams)
        // [END view_item_list]

        // [START select_item]
        val selectItemParams = Bundle()
        selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
        selectItemParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
        selectItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, selectItemParams)
        // [END select_item]

        // [START view_product_details]
        val viewItemParams = Bundle()
        viewItemParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        viewItemParams.putDouble(FirebaseAnalytics.Param.VALUE, 9.99)
        viewItemParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, viewItemParams)
        // [END view_product_details]

        // [START add_to_cart_wishlist]
        val itemJegginsWishlist = Bundle(itemJeggings)
        itemJegginsWishlist.putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        val addToWishlistParams = Bundle()
        addToWishlistParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        addToWishlistParams.putDouble(FirebaseAnalytics.Param.VALUE, 2 * 9.99)
        addToWishlistParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJegginsWishlist))

        analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, addToWishlistParams)
        // [END add_to_cart_wishlist]

        // [START view_cart]
        val itemJeggingsCart = Bundle(itemJeggings)
        itemJeggingsCart.putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        val itemBootsCart = Bundle(itemBoots)
        itemBootsCart.putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        val viewCartParams = Bundle()
        viewCartParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        viewCartParams.putDouble(FirebaseAnalytics.Param.VALUE, 2 * 9.99 + 1 * 24.99)
        viewCartParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart, itemBootsCart))

        analytics.logEvent(FirebaseAnalytics.Event.VIEW_CART, viewCartParams)
        // [END view_cart]

        // [START remove_from_cart]
        val removeCartParams = Bundle()
        removeCartParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        removeCartParams.putDouble(FirebaseAnalytics.Param.VALUE, 1 * 24.99)
        removeCartParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemBootsCart))

        analytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, removeCartParams)
        // [END remove_from_cart]

        // [START start_checkout]
        val beginCheckoutParams = Bundle()
        beginCheckoutParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        beginCheckoutParams.putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
        beginCheckoutParams.putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
        beginCheckoutParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))

        analytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, beginCheckoutParams)
        // [END start_checkout]

        // [START add_shipping]
        val addShippingParams = Bundle()
        addShippingParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        addShippingParams.putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
        addShippingParams.putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
        addShippingParams.putString(FirebaseAnalytics.Param.SHIPPING_TIER, "Ground")
        addShippingParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))

        analytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO, addShippingParams)
        // [END add_shipping]

        // [START add_payment]
        val addPaymentParams = Bundle()
        addPaymentParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        addPaymentParams.putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
        addPaymentParams.putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
        addPaymentParams.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, "Visa")
        addPaymentParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))

        analytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, addPaymentParams)
        // [END add_payment]

        // [START log_purchase]
        val purchaseParams = Bundle()
        purchaseParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
        purchaseParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
        purchaseParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        purchaseParams.putDouble(FirebaseAnalytics.Param.VALUE, 14.98)
        purchaseParams.putDouble(FirebaseAnalytics.Param.TAX, 2.58)
        purchaseParams.putDouble(FirebaseAnalytics.Param.SHIPPING, 5.34)
        purchaseParams.putString(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
        purchaseParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggingsCart))

        analytics.logEvent(FirebaseAnalytics.Event.PURCHASE, purchaseParams)
        // [END log_purchase]

        // [START log_refund]
        val refundParams = Bundle()
        refundParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
        refundParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
        refundParams.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
        refundParams.putDouble(FirebaseAnalytics.Param.VALUE, 9.99)

        // (Optional) for partial refunds, define the item ID and quantity of refunded items
        refundParams.putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
        refundParams.putLong(FirebaseAnalytics.Param.QUANTITY, 1)

        refundParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))

        analytics.logEvent(FirebaseAnalytics.Event.REFUND, refundParams)
        // [END log_refund]

        // [START apply_promo]
        val promoParams = Bundle()
        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_ID, "SUMMER_FUN")
        promoParams.putString(FirebaseAnalytics.Param.PROMOTION_NAME, "Summer Sale")
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg")
        promoParams.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1")
        promoParams.putString(FirebaseAnalytics.Param.LOCATION_ID, "HERO_BANNER")
        promoParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))

        // Promotion displayed
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams)

        // Promotion selected
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams)
        // [END apply_promo]
    }
}
