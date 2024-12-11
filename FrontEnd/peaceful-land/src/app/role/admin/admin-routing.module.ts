import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DefaultComponent } from './default/default.component';
import { HomeComponent } from './home/home.component';
import { PostPendingComponent } from './post-pending/post-pending.component';
import { HandlePostPendingComponent } from './post-pending/handle-post-pending/handle-post-pending.component';

const routes: Routes = [
  {path: "", component: DefaultComponent,
    children: [
      {path: "home", component: PostPendingComponent},
      {path: "post_pending" ,
        children: [
          {path: "", component: PostPendingComponent},
          {path: "handle/:id", component: HandlePostPendingComponent},
        ]
      },
      {path: "", component: PostPendingComponent, pathMatch: 'full'}
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
