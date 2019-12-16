<template>

</template>

<script>
export default {
  props: {
    name: String,
    level: Number,
    maxLevel: Number,
    price: {
        wood: Number,
        clay: Number,
        iron: Number,
        },
  },
}
</script>

<style>
.md-content {
  width: 200px;
  height: 200px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}
</style>
