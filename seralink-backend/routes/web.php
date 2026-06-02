<?php
use Illuminate\Support\Facades\Route;
use App\Livewire\Admin\Dashboard;

Route::get('/admin', Dashboard::class);