import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DefaultComponent } from './default/default.component';
import { HomeComponent } from './home/home.component';
import { PostPendingComponent } from './post-pending/post-pending.component';
import { HandlePostPendingComponent } from './post-pending/handle-post-pending/handle-post-pending.component';
import { PostApprovedComponent } from './post-approved/post-approved.component';
import { DetailPostApprovedComponent } from './post-approved/detail-post-approved/detail-post-approved.component';
import { PostRejectedComponent } from './post-rejected/post-rejected.component';
import { DetailPostRejectedComponent } from './post-rejected/detail-post-rejected/detail-post-rejected.component';

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
      {path: "post_approved" ,
        children: [
          {path: "", component: PostApprovedComponent},
          {path: "detail/:id", component: DetailPostApprovedComponent},
        ]
      },
      {path: "post_rejected" ,
        children: [
          {path: "", component: PostRejectedComponent},
          {path: "detail/:id", component: DetailPostRejectedComponent},
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
