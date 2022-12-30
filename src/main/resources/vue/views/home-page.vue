<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">BMI Information</h5>
          <div class="card-body">
            <h5 class="card-title">{{bmi.length}} bmi records</h5>
            <a href="/bmi" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Food calorie tracker</h5>
          <div class="card-body">
            <h5 class="card-title">{{food.length}} food records</h5>
            <a href="/foods" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        bmi: [],
        food: [],
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/bmi")
            .then(res => this.bmi = res.data)
            .catch(() => alert("Error while fetching bmi information"));
        axios.get("/api/foods")
            .then(res => this.food = res.data)
            .catch(() => alert("Error while fetching food information"));
      }
    });
</script>