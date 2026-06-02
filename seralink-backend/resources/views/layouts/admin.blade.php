<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SeraLink Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    @livewireStyles
</head>
<body class="bg-gray-100 font-sans">
    <nav class="bg-gray-700 text-white px-6 py-4 flex items-center justify-between">
        <span class="text-xl font-bold text-green-400">SeraLink Admin</span>
        <span class="text-sm text-gray-300">Tableau de bord</span>
    </nav>
    <main class="p-6">
        {{ $slot }}
    </main>
    @livewireScripts
</body>
</html>