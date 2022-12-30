<template id="bmi-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this bmi record.</p>
      <p> View <a :href="'/bmi'">all bmi records</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> BMI Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateBmi()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteBmi()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" style="width: 120px;" id="input-bmi-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="bmi.description" name="description" placeholder="Description"/>
            </div>
              <div class="input-group mb-3">
              <div class="input-group-prepend ">
                <span class="input-group-text" style="width: 120px;" id="input-bmi-height">Height</span>
              </div>
              <input type="number" class="form-control" v-model="bmi.height" placeholder="Height" name="height">
              &nbsp&nbsp
              <div class="input-group-prepend ">
                <span class="input-group-text" style="width: 120px;" id="input-bmi-weight">Weight</span>
              </div>
              <input type="number" class="form-control" v-model="bmi.weight" placeholder="Weight" name="weight">
            </div>
        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("bmi-profile", {
  template: "#bmi-profile",
  data: () => ({
    user: null,
    activity: null,
    noBMIRecordFound: false,
    noUserFound: false,
    bmi: [],
  }),
  created: function () {
    const bmiId = this.$javalin.pathParams["bmi-id"];
    const url = `/api/bmi/${bmiId}`
    axios.get(url)
        .then(res => this.bmi = res.data)
        .catch(error => {
          console.log("No bmi record found for id passed in the path parameter: " + error)
          this.noBMIRecordFound = true
        })
  },
  methods: {
    updateBmi: function () {
      const bmiId = this.$javalin.pathParams["bmi-id"];
      const timestamp = new Date().toISOString().slice(0, 30);
      const url = `/api/bmi/${bmiId}`
      axios.patch(url,
          {
            userId: this.bmi.userId,
            id: this.bmi.id,
            timestamp: timestamp,
            description: this.bmi.description,
            height: this.bmi.height,
            weight: this.bmi.weight,
            bmi: this.bmi.bmi,
          })
          .then(response =>
              this.bmi.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("BMI updated!")
    },
    deleteBmi: function () {
      if (confirm("Do you really want to delete?")) {
        const bmiId = this.$javalin.pathParams["bmi-id"];
        const url = `/api/bmi/${bmiId}`
        axios.delete(url)
            .then(response => {
              alert("BMI record deleted")
              //display the /bmi endpoint
              window.location.href = '/bmi';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>