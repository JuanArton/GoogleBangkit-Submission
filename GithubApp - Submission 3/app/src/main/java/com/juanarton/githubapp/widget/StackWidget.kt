package com.juanarton.githubapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.juanarton.githubapp.ListFavoriteActivity
import com.juanarton.githubapp.R

class StackWidget : AppWidgetProvider() {
    companion object {

        private const val ACTIVITY_ACTION = "com.juanarton.githubapp.ACTIVITY_ACTION"
        const val EXTRA_ITEM = "com.juanarton.githubapp.EXTRA_ITEM"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val views = RemoteViews(context.packageName, R.layout.stack_widget)
            views.setRemoteAdapter(R.id.stackWidgetView, intent)
            views.setEmptyView(R.id.stackWidgetView, R.id.empty_view)

            val openActivityIntent = Intent(context, StackWidget::class.java)
            openActivityIntent.action = ACTIVITY_ACTION
            val activityIntent = PendingIntent.getBroadcast(
                context,
                0,
                openActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stackWidgetView, activityIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == ACTIVITY_ACTION) {
                val jumpToActivity = Intent(context, ListFavoriteActivity::class.java)
                jumpToActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(jumpToActivity)
            }
        }
    }
}