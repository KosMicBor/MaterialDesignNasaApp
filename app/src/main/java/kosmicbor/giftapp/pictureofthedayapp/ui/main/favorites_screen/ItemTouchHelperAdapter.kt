package kosmicbor.giftapp.pictureofthedayapp.ui.main.favorites_screen

interface ItemTouchHelperAdapter {

    fun itemMove(fromPosition: Int, toPosition: Int)

    fun itemDismiss(position: Int)
}