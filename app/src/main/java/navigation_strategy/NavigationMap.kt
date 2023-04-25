package navigation_strategy

import edu.bluejack22_2.BeeTech.R

object NavigationMap {
    private  val homeStrategy = HomeStrategy()
    private  val adminHomeStrategy = AdminHomeStrategy()
    private  val searchStrategy = SearchStrategy()
    private  val createStrategy = CreateStrategy()
    private  val listStrategy = ListStrategy()
    private  val profileStrategy = ProfileStrategy()
    val userMap = mapOf(
        R.id.home_btn to homeStrategy,
        R.id.search_btn to searchStrategy,
        R.id.add_btn to createStrategy,
        R.id.list_btn to listStrategy,
        R.id.profile_btn to profileStrategy,
    )
    val adminMap = mapOf(
        R.id.home_btn to adminHomeStrategy,
        R.id.search_btn to searchStrategy,
        R.id.add_btn to createStrategy,
        R.id.list_btn to listStrategy,
        R.id.profile_btn to profileStrategy,
    )
}