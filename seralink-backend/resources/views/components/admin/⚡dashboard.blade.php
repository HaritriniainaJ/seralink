<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SeraLink Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    @livewireStyles
</head>
<body class="bg-gray-100">
    <nav class="bg-white shadow px-6 py-4 flex items-center justify-between">
        <span class="text-green-500 font-bold text-xl">SeraLink Admin</span>
        <span class="text-gray-500 text-sm">Tableau de bord</span>
    </nav>
    <main class="p-6">
        {{ $slot }}
    </main>
    @livewireScripts
</body>
</html>