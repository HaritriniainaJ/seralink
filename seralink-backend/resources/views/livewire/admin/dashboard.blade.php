<div>
    <h1 class="text-2xl font-bold text-gray-700 mb-6">Tableau de bord</h1>

    <div class="grid grid-cols-3 gap-4 mb-8">
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Total utilisateurs</p>
            <p class="text-3xl font-bold text-gray-700">{{ $totalUsers }}</p>
        </div>
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Freelances</p>
            <p class="text-3xl font-bold text-green-500">{{ $totalFreelances }}</p>
        </div>
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Clients</p>
            <p class="text-3xl font-bold text-gray-700">{{ $totalClients }}</p>
        </div>
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Total missions</p>
            <p class="text-3xl font-bold text-gray-700">{{ $totalJobs }}</p>
        </div>
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Missions ouvertes</p>
            <p class="text-3xl font-bold text-green-500">{{ $openJobs }}</p>
        </div>
        <div class="bg-white rounded-lg p-5 shadow">
            <p class="text-gray-500 text-sm">Contrats</p>
            <p class="text-3xl font-bold text-gray-700">{{ $totalContracts }}</p>
        </div>
    </div>
</div>