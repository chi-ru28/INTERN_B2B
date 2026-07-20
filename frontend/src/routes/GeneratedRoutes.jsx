import React from 'react';
import { Route } from 'react-router-dom';
import SplashScreen_public from '../pages/generated/public/SplashScreen';
import AboutUs_public from '../pages/generated/public/AboutUs';
import ContactUs_public from '../pages/generated/public/ContactUs';
import FAQ_public from '../pages/generated/public/FAQ';
import PrivacyPolicy_public from '../pages/generated/public/PrivacyPolicy';
import TermsConditions_public from '../pages/generated/public/TermsConditions';
import ProductDetails_public from '../pages/generated/public/ProductDetails';
import SearchResults_public from '../pages/generated/public/SearchResults';
import Login_auth from '../pages/generated/auth/Login';
import Register_auth from '../pages/generated/auth/Register';
import EmailVerification_auth from '../pages/generated/auth/EmailVerification';
import ForgotPassword_auth from '../pages/generated/auth/ForgotPassword';
import ResetPassword_auth from '../pages/generated/auth/ResetPassword';
import OtpVerification_auth from '../pages/generated/auth/OtpVerification';
import ChangePassword_auth from '../pages/generated/auth/ChangePassword';
import ProfileSetup_auth from '../pages/generated/auth/ProfileSetup';
import Dashboard_customer from '../pages/generated/customer/Dashboard';
import AddressBook_customer from '../pages/generated/customer/AddressBook';
import Wishlist_customer from '../pages/generated/customer/Wishlist';
import ShoppingCart_customer from '../pages/generated/customer/ShoppingCart';
import Checkout_customer from '../pages/generated/customer/Checkout';
import Payment_customer from '../pages/generated/customer/Payment';
import OrderSuccess_customer from '../pages/generated/customer/OrderSuccess';
import OrderFailed_customer from '../pages/generated/customer/OrderFailed';
import OrderDetails_customer from '../pages/generated/customer/OrderDetails';
import Invoice_customer from '../pages/generated/customer/Invoice';
import Notifications_customer from '../pages/generated/customer/Notifications';
import AllProducts_product from '../pages/generated/product/AllProducts';
import Categories_product from '../pages/generated/product/Categories';
import Brands_product from '../pages/generated/product/Brands';
import ProductReviews_product from '../pages/generated/product/ProductReviews';
import RecommendedProducts_product from '../pages/generated/product/RecommendedProducts';
import RecentlyViewed_product from '../pages/generated/product/RecentlyViewed';
import OffersDiscounts_product from '../pages/generated/product/OffersDiscounts';
import Cart_checkout from '../pages/generated/checkout/Cart';
import ShippingAddress_checkout from '../pages/generated/checkout/ShippingAddress';
import DeliveryOptions_checkout from '../pages/generated/checkout/DeliveryOptions';
import OrderSummary_checkout from '../pages/generated/checkout/OrderSummary';
import PaymentGateway_checkout from '../pages/generated/checkout/PaymentGateway';
import PaymentSuccess_checkout from '../pages/generated/checkout/PaymentSuccess';
import PaymentFailed_checkout from '../pages/generated/checkout/PaymentFailed';
import OrderConfirmation_checkout from '../pages/generated/checkout/OrderConfirmation';
import VendorDashboard_vendor from '../pages/generated/vendor/VendorDashboard';
import MyProducts_vendor from '../pages/generated/vendor/MyProducts';
import AddProduct_vendor from '../pages/generated/vendor/AddProduct';
import EditProduct_vendor from '../pages/generated/vendor/EditProduct';
import Inventory_vendor from '../pages/generated/vendor/Inventory';
import Orders_vendor from '../pages/generated/vendor/Orders';
import SalesAnalytics_vendor from '../pages/generated/vendor/SalesAnalytics';
import Earnings_vendor from '../pages/generated/vendor/Earnings';
import CustomerReviews_vendor from '../pages/generated/vendor/CustomerReviews';
import Notifications_vendor from '../pages/generated/vendor/Notifications';
import Settings_vendor from '../pages/generated/vendor/Settings';
import Dashboard_admin from '../pages/generated/admin/Dashboard';
import Analytics_admin from '../pages/generated/admin/Analytics';
import UserManagement_admin from '../pages/generated/admin/UserManagement';
import VendorManagement_admin from '../pages/generated/admin/VendorManagement';
import ProductManagement_admin from '../pages/generated/admin/ProductManagement';
import CategoryManagement_admin from '../pages/generated/admin/CategoryManagement';
import BrandManagement_admin from '../pages/generated/admin/BrandManagement';
import InventoryManagement_admin from '../pages/generated/admin/InventoryManagement';
import OrderManagement_admin from '../pages/generated/admin/OrderManagement';
import PaymentManagement_admin from '../pages/generated/admin/PaymentManagement';
import CouponManagement_admin from '../pages/generated/admin/CouponManagement';
import NotificationManagement_admin from '../pages/generated/admin/NotificationManagement';
import Reports_admin from '../pages/generated/admin/Reports';
import Logs_admin from '../pages/generated/admin/Logs';
import RolesPermissions_admin from '../pages/generated/admin/RolesPermissions';
import Settings_admin from '../pages/generated/admin/Settings';
import InventoryDashboard_inventory from '../pages/generated/inventory/InventoryDashboard';
import StockList_inventory from '../pages/generated/inventory/StockList';
import LowStock_inventory from '../pages/generated/inventory/LowStock';
import Warehouse_inventory from '../pages/generated/inventory/Warehouse';
import StockMovement_inventory from '../pages/generated/inventory/StockMovement';
import InventoryHistory_inventory from '../pages/generated/inventory/InventoryHistory';
import AllOrders_order from '../pages/generated/order/AllOrders';
import PendingOrders_order from '../pages/generated/order/PendingOrders';
import Processing_order from '../pages/generated/order/Processing';
import Shipped_order from '../pages/generated/order/Shipped';
import Delivered_order from '../pages/generated/order/Delivered';
import Cancelled_order from '../pages/generated/order/Cancelled';
import Returns_order from '../pages/generated/order/Returns';
import RefundRequests_order from '../pages/generated/order/RefundRequests';
import PaymentDashboard_payment from '../pages/generated/payment/PaymentDashboard';
import Transactions_payment from '../pages/generated/payment/Transactions';
import PaymentHistory_payment from '../pages/generated/payment/PaymentHistory';
import Refunds_payment from '../pages/generated/payment/Refunds';
import FailedPayments_payment from '../pages/generated/payment/FailedPayments';
import PaymentAnalytics_payment from '../pages/generated/payment/PaymentAnalytics';
import NotificationCenter_notification from '../pages/generated/notification/NotificationCenter';
import EmailTemplates_notification from '../pages/generated/notification/EmailTemplates';
import SmsTemplates_notification from '../pages/generated/notification/SmsTemplates';
import PushNotifications_notification from '../pages/generated/notification/PushNotifications';
import NotificationHistory_notification from '../pages/generated/notification/NotificationHistory';
import SalesDashboard_analytics from '../pages/generated/analytics/SalesDashboard';
import Revenue_analytics from '../pages/generated/analytics/Revenue';
import ProductAnalytics_analytics from '../pages/generated/analytics/ProductAnalytics';
import CustomerAnalytics_analytics from '../pages/generated/analytics/CustomerAnalytics';
import VendorAnalytics_analytics from '../pages/generated/analytics/VendorAnalytics';
import InventoryAnalytics_analytics from '../pages/generated/analytics/InventoryAnalytics';
import PaymentAnalytics_analytics from '../pages/generated/analytics/PaymentAnalytics';
import Users_user from '../pages/generated/user/Users';
import UserDetails_user from '../pages/generated/user/UserDetails';
import Roles_user from '../pages/generated/user/Roles';
import Permissions_user from '../pages/generated/user/Permissions';
import ActivityLogs_user from '../pages/generated/user/ActivityLogs';
import GeneralSettings_settings from '../pages/generated/settings/GeneralSettings';
import SecuritySettings_settings from '../pages/generated/settings/SecuritySettings';
import OauthJwtSettings_settings from '../pages/generated/settings/OauthJwtSettings';
import EmailSettings_settings from '../pages/generated/settings/EmailSettings';
import PaymentGatewaySettings_settings from '../pages/generated/settings/PaymentGatewaySettings';
import RedisCacheSettings_settings from '../pages/generated/settings/RedisCacheSettings';
import KafkaSettings_settings from '../pages/generated/settings/KafkaSettings';
import ApiKeys_settings from '../pages/generated/settings/ApiKeys';
import BackupRestore_settings from '../pages/generated/settings/BackupRestore';
import Unauthorized401_error from '../pages/generated/error/Unauthorized401';
import Forbidden403_error from '../pages/generated/error/Forbidden403';
import NotFound404_error from '../pages/generated/error/NotFound404';
import InternalServerError500_error from '../pages/generated/error/InternalServerError500';
import MaintenanceMode_error from '../pages/generated/error/MaintenanceMode';
import ApiDocumentation_developer from '../pages/generated/developer/ApiDocumentation';
import HealthCheck_developer from '../pages/generated/developer/HealthCheck';
import ServiceStatus_developer from '../pages/generated/developer/ServiceStatus';
import KafkaEventsMonitor_developer from '../pages/generated/developer/KafkaEventsMonitor';
import LogsDashboard_developer from '../pages/generated/developer/LogsDashboard';
import MetricsDashboard_developer from '../pages/generated/developer/MetricsDashboard';

export const GeneratedRoutes = (
  <>
    <Route path="/generated/public/splashscreen" element={<SplashScreen_public />} />
    <Route path="/generated/public/aboutus" element={<AboutUs_public />} />
    <Route path="/generated/public/contactus" element={<ContactUs_public />} />
    <Route path="/generated/public/faq" element={<FAQ_public />} />
    <Route path="/generated/public/privacypolicy" element={<PrivacyPolicy_public />} />
    <Route path="/generated/public/termsconditions" element={<TermsConditions_public />} />
    <Route path="/generated/public/productdetails" element={<ProductDetails_public />} />
    <Route path="/generated/public/searchresults" element={<SearchResults_public />} />
    <Route path="/generated/auth/login" element={<Login_auth />} />
    <Route path="/generated/auth/register" element={<Register_auth />} />
    <Route path="/generated/auth/emailverification" element={<EmailVerification_auth />} />
    <Route path="/generated/auth/forgotpassword" element={<ForgotPassword_auth />} />
    <Route path="/generated/auth/resetpassword" element={<ResetPassword_auth />} />
    <Route path="/generated/auth/otpverification" element={<OtpVerification_auth />} />
    <Route path="/generated/auth/changepassword" element={<ChangePassword_auth />} />
    <Route path="/generated/auth/profilesetup" element={<ProfileSetup_auth />} />
    <Route path="/generated/customer/dashboard" element={<Dashboard_customer />} />
    <Route path="/generated/customer/addressbook" element={<AddressBook_customer />} />
    <Route path="/generated/customer/wishlist" element={<Wishlist_customer />} />
    <Route path="/generated/customer/shoppingcart" element={<ShoppingCart_customer />} />
    <Route path="/generated/customer/checkout" element={<Checkout_customer />} />
    <Route path="/generated/customer/payment" element={<Payment_customer />} />
    <Route path="/generated/customer/ordersuccess" element={<OrderSuccess_customer />} />
    <Route path="/generated/customer/orderfailed" element={<OrderFailed_customer />} />
    <Route path="/generated/customer/orderdetails" element={<OrderDetails_customer />} />
    <Route path="/generated/customer/invoice" element={<Invoice_customer />} />
    <Route path="/generated/customer/notifications" element={<Notifications_customer />} />
    <Route path="/generated/product/allproducts" element={<AllProducts_product />} />
    <Route path="/generated/product/categories" element={<Categories_product />} />
    <Route path="/generated/product/brands" element={<Brands_product />} />
    <Route path="/generated/product/productreviews" element={<ProductReviews_product />} />
    <Route path="/generated/product/recommendedproducts" element={<RecommendedProducts_product />} />
    <Route path="/generated/product/recentlyviewed" element={<RecentlyViewed_product />} />
    <Route path="/generated/product/offersdiscounts" element={<OffersDiscounts_product />} />
    <Route path="/generated/checkout/cart" element={<Cart_checkout />} />
    <Route path="/generated/checkout/shippingaddress" element={<ShippingAddress_checkout />} />
    <Route path="/generated/checkout/deliveryoptions" element={<DeliveryOptions_checkout />} />
    <Route path="/generated/checkout/ordersummary" element={<OrderSummary_checkout />} />
    <Route path="/generated/checkout/paymentgateway" element={<PaymentGateway_checkout />} />
    <Route path="/generated/checkout/paymentsuccess" element={<PaymentSuccess_checkout />} />
    <Route path="/generated/checkout/paymentfailed" element={<PaymentFailed_checkout />} />
    <Route path="/generated/checkout/orderconfirmation" element={<OrderConfirmation_checkout />} />
    <Route path="/generated/vendor/vendordashboard" element={<VendorDashboard_vendor />} />
    <Route path="/generated/vendor/myproducts" element={<MyProducts_vendor />} />
    <Route path="/generated/vendor/addproduct" element={<AddProduct_vendor />} />
    <Route path="/generated/vendor/editproduct" element={<EditProduct_vendor />} />
    <Route path="/generated/vendor/inventory" element={<Inventory_vendor />} />
    <Route path="/generated/vendor/orders" element={<Orders_vendor />} />
    <Route path="/generated/vendor/salesanalytics" element={<SalesAnalytics_vendor />} />
    <Route path="/generated/vendor/earnings" element={<Earnings_vendor />} />
    <Route path="/generated/vendor/customerreviews" element={<CustomerReviews_vendor />} />
    <Route path="/generated/vendor/notifications" element={<Notifications_vendor />} />
    <Route path="/generated/vendor/settings" element={<Settings_vendor />} />
    <Route path="/generated/admin/dashboard" element={<Dashboard_admin />} />
    <Route path="/generated/admin/analytics" element={<Analytics_admin />} />
    <Route path="/generated/admin/usermanagement" element={<UserManagement_admin />} />
    <Route path="/generated/admin/vendormanagement" element={<VendorManagement_admin />} />
    <Route path="/generated/admin/productmanagement" element={<ProductManagement_admin />} />
    <Route path="/generated/admin/categorymanagement" element={<CategoryManagement_admin />} />
    <Route path="/generated/admin/brandmanagement" element={<BrandManagement_admin />} />
    <Route path="/generated/admin/inventorymanagement" element={<InventoryManagement_admin />} />
    <Route path="/generated/admin/ordermanagement" element={<OrderManagement_admin />} />
    <Route path="/generated/admin/paymentmanagement" element={<PaymentManagement_admin />} />
    <Route path="/generated/admin/couponmanagement" element={<CouponManagement_admin />} />
    <Route path="/generated/admin/notificationmanagement" element={<NotificationManagement_admin />} />
    <Route path="/generated/admin/reports" element={<Reports_admin />} />
    <Route path="/generated/admin/logs" element={<Logs_admin />} />
    <Route path="/generated/admin/rolespermissions" element={<RolesPermissions_admin />} />
    <Route path="/generated/admin/settings" element={<Settings_admin />} />
    <Route path="/generated/inventory/inventorydashboard" element={<InventoryDashboard_inventory />} />
    <Route path="/generated/inventory/stocklist" element={<StockList_inventory />} />
    <Route path="/generated/inventory/lowstock" element={<LowStock_inventory />} />
    <Route path="/generated/inventory/warehouse" element={<Warehouse_inventory />} />
    <Route path="/generated/inventory/stockmovement" element={<StockMovement_inventory />} />
    <Route path="/generated/inventory/inventoryhistory" element={<InventoryHistory_inventory />} />
    <Route path="/generated/order/allorders" element={<AllOrders_order />} />
    <Route path="/generated/order/pendingorders" element={<PendingOrders_order />} />
    <Route path="/generated/order/processing" element={<Processing_order />} />
    <Route path="/generated/order/shipped" element={<Shipped_order />} />
    <Route path="/generated/order/delivered" element={<Delivered_order />} />
    <Route path="/generated/order/cancelled" element={<Cancelled_order />} />
    <Route path="/generated/order/returns" element={<Returns_order />} />
    <Route path="/generated/order/refundrequests" element={<RefundRequests_order />} />
    <Route path="/generated/payment/paymentdashboard" element={<PaymentDashboard_payment />} />
    <Route path="/generated/payment/transactions" element={<Transactions_payment />} />
    <Route path="/generated/payment/paymenthistory" element={<PaymentHistory_payment />} />
    <Route path="/generated/payment/refunds" element={<Refunds_payment />} />
    <Route path="/generated/payment/failedpayments" element={<FailedPayments_payment />} />
    <Route path="/generated/payment/paymentanalytics" element={<PaymentAnalytics_payment />} />
    <Route path="/generated/notification/notificationcenter" element={<NotificationCenter_notification />} />
    <Route path="/generated/notification/emailtemplates" element={<EmailTemplates_notification />} />
    <Route path="/generated/notification/smstemplates" element={<SmsTemplates_notification />} />
    <Route path="/generated/notification/pushnotifications" element={<PushNotifications_notification />} />
    <Route path="/generated/notification/notificationhistory" element={<NotificationHistory_notification />} />
    <Route path="/generated/analytics/salesdashboard" element={<SalesDashboard_analytics />} />
    <Route path="/generated/analytics/revenue" element={<Revenue_analytics />} />
    <Route path="/generated/analytics/productanalytics" element={<ProductAnalytics_analytics />} />
    <Route path="/generated/analytics/customeranalytics" element={<CustomerAnalytics_analytics />} />
    <Route path="/generated/analytics/vendoranalytics" element={<VendorAnalytics_analytics />} />
    <Route path="/generated/analytics/inventoryanalytics" element={<InventoryAnalytics_analytics />} />
    <Route path="/generated/analytics/paymentanalytics" element={<PaymentAnalytics_analytics />} />
    <Route path="/generated/user/users" element={<Users_user />} />
    <Route path="/generated/user/userdetails" element={<UserDetails_user />} />
    <Route path="/generated/user/roles" element={<Roles_user />} />
    <Route path="/generated/user/permissions" element={<Permissions_user />} />
    <Route path="/generated/user/activitylogs" element={<ActivityLogs_user />} />
    <Route path="/generated/settings/generalsettings" element={<GeneralSettings_settings />} />
    <Route path="/generated/settings/securitysettings" element={<SecuritySettings_settings />} />
    <Route path="/generated/settings/oauthjwtsettings" element={<OauthJwtSettings_settings />} />
    <Route path="/generated/settings/emailsettings" element={<EmailSettings_settings />} />
    <Route path="/generated/settings/paymentgatewaysettings" element={<PaymentGatewaySettings_settings />} />
    <Route path="/generated/settings/rediscachesettings" element={<RedisCacheSettings_settings />} />
    <Route path="/generated/settings/kafkasettings" element={<KafkaSettings_settings />} />
    <Route path="/generated/settings/apikeys" element={<ApiKeys_settings />} />
    <Route path="/generated/settings/backuprestore" element={<BackupRestore_settings />} />
    <Route path="/generated/error/unauthorized401" element={<Unauthorized401_error />} />
    <Route path="/generated/error/forbidden403" element={<Forbidden403_error />} />
    <Route path="/generated/error/notfound404" element={<NotFound404_error />} />
    <Route path="/generated/error/internalservererror500" element={<InternalServerError500_error />} />
    <Route path="/generated/error/maintenancemode" element={<MaintenanceMode_error />} />
    <Route path="/generated/developer/apidocumentation" element={<ApiDocumentation_developer />} />
    <Route path="/generated/developer/healthcheck" element={<HealthCheck_developer />} />
    <Route path="/generated/developer/servicestatus" element={<ServiceStatus_developer />} />
    <Route path="/generated/developer/kafkaeventsmonitor" element={<KafkaEventsMonitor_developer />} />
    <Route path="/generated/developer/logsdashboard" element={<LogsDashboard_developer />} />
    <Route path="/generated/developer/metricsdashboard" element={<MetricsDashboard_developer />} />
  </>
);
