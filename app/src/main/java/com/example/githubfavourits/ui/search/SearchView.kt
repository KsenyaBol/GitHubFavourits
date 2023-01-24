package com.example.githubfavourits.ui.search

import com.example.domain.entity.Repositories
import com.example.githubfavourits.ui.base.BaseView
import com.omegar.mvp.viewstate.strategy.StateStrategyType
import com.omegar.mvp.viewstate.strategy.StrategyType

interface SearchView:BaseView {

@StateStrategyType(StrategyType.ADD_TO_END_SINGLE)
fun setRepository(repositories: List<Repositories>)

}