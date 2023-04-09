/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.scenes.UserDetailsCtrl;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    /**
     * Configure method
     * @param binder the binder to which controllers are bound
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(DeListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(NewCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(RnListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(SelectServerCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ConfirmUsernameCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(JoinBoardByIDCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserDetailsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddTagToCardCtrl.class).in(Scopes.SINGLETON);
    }
}