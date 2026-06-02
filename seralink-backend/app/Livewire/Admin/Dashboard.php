<?php
namespace App\Livewire\Admin;

use App\Models\User;
use App\Models\JobListing;
use App\Models\Contract;
use Livewire\Component;

class Dashboard extends Component
{
    public function render()
    {
        return view('livewire.admin.dashboard', [
            'totalUsers'      => User::count(),
            'totalFreelances' => User::where('role', 'freelance')->count(),
            'totalClients'    => User::where('role', 'client')->count(),
            'totalJobs'       => JobListing::count(),
            'openJobs'        => JobListing::where('status', 'open')->count(),
            'totalContracts'  => Contract::count(),
        ])->layout('layouts.admin');
    }
}